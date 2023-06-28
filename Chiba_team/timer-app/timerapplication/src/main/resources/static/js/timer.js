function startBtnClick() {
  var data = {
    time: $("#set_time").val(),
    timeZone: $('input:radio[name="timezone"]:checked').val(),
  };
  $.ajax({
    url: $("#timer_form").attr("action"), // リクエストを送信するURLを指定（action属性のurlを抽出）
    type: "POST", // HTTPメソッドを指定
    data: JSON.stringify(data),
    contentType: "application/json", // リクエストの Content-Type
  })
    .done(function (response) {
      alert(response);
    })
    .fail(function () {
      alert("通信エラー");
    });
}

function stopBtnClick() {
  $.ajax({
    url: "/timer/stop", // リクエストを送信するURLを指定
    type: "POST", // HTTPメソッドを指定
    data: {},
    contentType: "application/json", // リクエストの Content-Type
  })
    .done(function (response) {
      alert(response);
    })
    .fail(function () {
      alert("通信エラー");
    });
}
