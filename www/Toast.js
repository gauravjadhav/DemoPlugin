function Toast() {
}

Toast.prototype.show = function (object, successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "Toast", "show", [object]);
};

Toast.prototype.showShortTop = function (object, successCallback, errorCallback) {
  this.show(object, successCallback, errorCallback);
};


Toast.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.toast = new Toast();
  return window.plugins.toast;
};

cordova.addConstructor(Toast.install);