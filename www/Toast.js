function Toast() {
}

Toast.prototype.show = function (filename, successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "Toast", "show", [{"filename": filename}]);
};

Toast.prototype.showShortTop = function (filename, successCallback, errorCallback) {
  this.show(filename, successCallback, errorCallback);
};


Toast.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.toast = new Toast();
  return window.plugins.toast;
};

cordova.addConstructor(Toast.install);