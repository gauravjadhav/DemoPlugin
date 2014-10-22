function Toast() {
}

Toast.prototype.show = function (filepath, successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "Toast", "show", [filepath]);
};

Toast.prototype.showShortTop = function (filepath, successCallback, errorCallback) {
  this.show(filepath, successCallback, errorCallback);
};

Toast.prototype.showShortCenter = function (message, successCallback, errorCallback) {
  this.show(message, "short", "center", successCallback, errorCallback);
};

Toast.prototype.showShortBottom = function (message, successCallback, errorCallback) {
  this.show(message, "short", "bottom", successCallback, errorCallback);
};

Toast.prototype.showLongTop = function (message, successCallback, errorCallback) {
  this.show(message, "long", "top", successCallback, errorCallback);
};

Toast.prototype.showLongCenter = function (message, successCallback, errorCallback) {
  this.show(message, "long", "center", successCallback, errorCallback);
};

Toast.prototype.showLongBottom = function (message, successCallback, errorCallback) {
  this.show(message, "long", "bottom", successCallback, errorCallback);
};

Toast.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.toast = new Toast();
  return window.plugins.toast;
};

cordova.addConstructor(Toast.install);