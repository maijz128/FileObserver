// ==UserScript==
// @name         FileObserver
// @namespace    https://github.com/maijz128
// @version      0.1.0
// @description  开发用。Update:2018-06-18
// @author       MaiJZ
// @require      https://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js
// @require      https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js
// @grant        none
// ==/UserScript==


(function () {
    function appendScript(src) {
        var f = document.createElement('script');
        f.src = src;
        document.body.appendChild(f);
    }
    appendScript('https://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js');
    appendScript('https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js');

    window.FileObserver = FileObserver;
    document.FileObserver = FileObserver;

    function FileObserver(option) {
        const self = this;
        this._isDebug = true;
        this._server = "http://localhost:8193";
        this._point = "/socket-point";
        this._topic = "/topic/getState";
        this._IS_CHANGED = "IS_CHANGED";

        this._isConnected = false;
        this.onConnected = null; // callback method

        this.stompClient = null;

        this._CheckWorker = setInterval(function(){
            if (!self.isConnected()) {
                if(self._isDebug) console.log('connect again ...');
                self.connect();
            }
        }, 3000);

    }
    FileObserver.prototype.setServer = function (server) {
        this._server = server || this._server;
    };
    FileObserver.prototype.canDebug = function (value) {
        this._isDebug = value ? true : false;
    };
    FileObserver.prototype.isConnected = function () {
        return this.stompClient.connected;
    };
    FileObserver.prototype.connect = function () {
        const self = this;
        //传递用户key值
        // var login = "ricky";
        var socket = new SockJS(self._server + self._point);
        self.stompClient = Stomp.over(socket);
        self.stompClient.connect({
                // login: login
            },
            function (frame) {
                self._setConnected(true);
                if (self._isDebug) console.log('Connected: ' + frame);

                self.stompClient.subscribe(self._topic, function (response) {
                    self._onResponse(response);
                });
            });
        // if (self._isDebug) console.log(self.stompClient);
    };
    FileObserver.prototype.disconnect = function () {
        const self = this;
        if (self.stompClient != null) {
            self.stompClient.disconnect();
        }
        self._setConnected(false);
        if (self._isDebug) console.log("Disconnected");
    };


    FileObserver.prototype._setConnected = function (connected) {
        const self = this;
        this._isConnected = connected ? true : false;
        if (this.onConnected) {
            setTimeout(function () {
                this.onConnected(connected);
            }, 1);
        }
    };
    FileObserver.prototype._onResponse = function (response) {
        var message = response.body;
        if (message === this._IS_CHANGED) {
            location.reload();
        }
        console.error(message);
    };
    // FileObserver.prototype.sendMessage = function () {
    //     const self = this;
    //     // self.stompClient.send("/app/msg/hellosingle", {}, JSON.stringify({
    //     //     'name': $("#name").val(),
    //     //     'id': 'rickyt2'
    //     // }));
    // };

})();