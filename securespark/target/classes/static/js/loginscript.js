/* global CryptoJS */
var login = (function () {

    function enter() {
        var info = {
            "username": $("#username").val(),
            "password": $("#password").val()
        };
        const promise = $.post({
            url: "/login",
            data: JSON.stringify(info),
            contentType: "application/json"
        });
        promise.then(function (data) {
            if (data === "true") {
                alert("Solicitando el servicio.");
                window.location.href = "/service?username=" + $("#username").val();
            } else {
                alert("Credenciales Incorrectas");
            }
        }, function (error) {
            alert("Try again");
        });
    }

    return {
        enter: enter
    };
})();
