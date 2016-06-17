$(function() {
  $('html, body').animate({scrollTop: 0}, '500');
  //登录校验
  $('#loginForm').validator({
    onValid: function(validity) {
      $(validity.field).closest('.am-form-group').find('.am-alert').hide();
    },

    onInValid: function(validity) {
      var $field = $(validity.field);
      var $group = $field.closest('.am-form-group');
      var $alert = $group.find('.am-alert');
      // 使用自定义的提示信息 或 插件内置的提示信息
      var msg = $field.attr('data-validation-message');

      if (!$alert.length) {
        $alert = $('<div class="am-alert am-alert-danger"></div>').hide().
          appendTo($group);
      }

      $alert.html(msg).show();
    }
  });

  //注册校验
  $('#registerForm').validator({
    validate: function(validity) {
      if ($(validity.field).is("#account")) {
        $("#account").attr("data-validation-message", "用户名只能4~12位！");
        var account = $("#account").val();
        if (!account) {
          $("#account").attr("data-validation-message", "请输入用户名！");
          validity.valid = false;
          return validity;
        }
        var reg2 = /^(\w|\d)+$/ig;
        var accountOk = reg2.test(account);
        if (accountOk) {
        }
        else{
          $("#account").attr("data-validation-message", "用户名只能由字母和数字组成！");
          validity.valid = false;
          return validity;
        }

        if(validity.valid){
          // 异步操作必须返回 Deferred 对象
          return $.post("isExsitAccount", {
          account : account
          },
          function(data){
            console.log(data.resultCode);
          }).then(function(data) {
          if (data.resultCode===0) {
            // validity.valid = true;
          }
          else{
            $("#account").attr("data-validation-message", "用户名已存在");
            validity.valid = false;
          }
            return validity;
          }, function() {
            return validity;
          });
        }
      }
      else if ($(validity.field).is("#password")) {
        $("#password").attr("data-validation-message", "密码只能6~15位！");
        var pwd = $("#password").val();
        if (!pwd) {
          $("#password").attr("data-validation-message", "请输入密码！");
          validity.valid = false;
          return validity;
        }
      }
      else if ($(validity.field).is("#confirmPwd")) {
        var pwd = $("#password").val();
        var confirmPwd = $("#confirmPwd").val();
        if (!confirmPwd) {
          $("#confirmPwd").attr("data-validation-message", "请确认密码！");
          validity.valid = false;
          return validity;
        }
        if (pwd != confirmPwd) {
          $("#confirmPwd").attr("data-validation-message", "两次输入密码不一致！");
          validity.valid = false;
          return validity;
        }
      }
      return validity;
    },
    onValid: function(validity) {
      $(validity.field).closest('.am-input-group').find('.am-alert').hide();
    },

    onInValid: function(validity) {
      var $field = $(validity.field);
      var $group = $field.closest('.am-input-group');
      var $alert = $group.find('.am-alert');
      // 使用自定义的提示信息 或 插件内置的提示信息
      var msg = $field.attr('data-validation-message');

      if (!$alert.length) {
        $alert = $('<div class="am-alert am-alert-danger"></div>').hide().
          appendTo($group);
      }

      $alert.html(msg).show();
    }
  });

  //获取用户名
  $(document).ready(function () {
    $('#login-account').val(getCookie('account'));
  });

  //上传logo
  $('#logoUpload').change(function(){
    var formData = new FormData($( "#logoForm" )[0]);
    $('#uploading').modal();
    $.ajax({
      url: '/user/logo' ,
      type: 'POST',
      data: formData,
      async: true,
      cache: false,
      contentType: false,
      processData: false,
      success: function (data) {
        $('#message').text(data.message);
        $('#uploading').modal('close');
        $('#result').modal();
        $('#logo').attr('src', data.path);
      },
      error: function (data) {
        $('#message').text("未知错误");
        $('#uploading').modal('close');
        $('#result').modal();
      }
    });
  });

  //获取已选择文件的文件名
  $('#bookUpload').change(function(){
    var book = $('#bookUpload')[0].files[0];
    $('#bookName').val(book.name.replace(".txt", "").replace(".TXT", ""));
    $('#choosed').text("已选文件：" + book.name);
  });

  //上传电子书
  $('#addBook').click(function(){
    var bookList = $('#bookUpload')[0].files;
    if (bookList.length == 0) {
      $('#message').text('请选择文件！');
      $('#result').modal();
      return;
    }
    if ($('#uploadBook').validator('isFormValid')) {
      $('#uploading').modal();
      var book = bookList[0];
      var bookName = $('#bookName').val();
      var tag = $('#tag').val();
      var isShare = $('input[name=isShare]:checked').val();
      var summary = $('#summary').val();
      var formData = new FormData();
      formData.append("book", book);
      formData.append("bookName", bookName);
      formData.append("tag", tag);
      formData.append("isShare", isShare);
      formData.append("summary", summary);
      $.ajax({
        url: '/book/addBook' ,
        type: 'POST',
        data: formData,
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
          $('#message').text(data.message);
          $('#uploading').modal('close');
          $('#result').modal({
            relatedTarget: this,
            onConfirm: function(options) {
              if (data.resultCode==0) {
                window.location.href = "/book/myBook";
              }
            }
          });
        },
        error: function (data) {
          $('#message').text("未知错误");
          $('#uploading').modal('close');
          $('#result').modal();
        }
      });
    }
  });

  //修改电子书信息
  $('#submitModifyBook').click(function(){
    if ($('#modifyBook').validator('isFormValid')){
      var bookId = $('#bookId').val();
      var bookName = $('#bookName').val();
      var tag = $('#tag').val();
      var isShare = $('input[name=isShare]:checked').val();
      var summary = $('#summary').val();

      $.post("/book/modify",{
        bookId : bookId,
        bookName : bookName,
        tag : tag,
        isShare : isShare,
        summary : summary
      },function(data){
        $('#message').text(data.message);
        $('#result').modal({
          relatedTarget: this,
          onConfirm: function(options) {
            if (data.resultCode==0) {
              window.location.href = "/book/detail?bookId=" + bookId;
            }
          }
        });
      });
    }
  });

  //修改个人信息验证
  $('#imf').validator({
    validate: function(validity){
      if ($(validity.field).is("#email")){
        validity.valid = /^[a-zA-Z0-9]+@[a-z0-9]+\.com$/g.test($('#email').val());
        return validity;
      }else if($(validity.field).is("#phone")){
        validity.valid = /^[0-9]{11,11}$/.test($('#phone').val());
        return validity;
      }else if ($(validity.field).is("#QQ")) {
        validity.valid = /^[0-9]{5,12}$/.test($('#QQ').val());
        return validity;
      }else if ($(validity.field).is("#textNumber")) {
        validity.valid = $('#textNumber').val()>1999 && $('#textNumber').val()<10000;
        return validity;
      }
    },
    onValid: function(validity) {
      // $(validity.field).closest('.am-form-group').find('.am-alert').hide();
    },

    onInValid: function(validity) {
      // var $field = $(validity.field);
      // var $group = $field.closest('.am-form-group');
      // var $alert = $group.find('.am-alert');
      // // 使用自定义的提示信息 或 插件内置的提示信息
      // var msg = $field.attr('data-validation-message');

      // if (!$alert.length) {
      //   $alert = $('<div class="am-alert am-alert-danger"></div>').hide().
      //     appendTo($group);
      // }

      // $alert.html(msg).show();
    }
  });

  //修改密码
  $('#modifyPwdForm').validator({
    onValid: function(validity) {
      $(validity.field).closest('.am-input-group').find('.am-alert').hide();
    },

    onInValid: function(validity) {
      var $field = $(validity.field);
      var $group = $field.closest('.am-input-group');
      var $alert = $group.find('.am-alert');
      // 使用自定义的提示信息 或 插件内置的提示信息
      var msg = $field.attr('data-validation-message');

      if (!$alert.length) {
        $alert = $('<div class="am-alert am-alert-danger"></div>').hide().
          appendTo($group);
      }

      $alert.html(msg).show();
    }
  });

  $('#modifyPwd').click(function(){
    if ($('#uploadBook').validator('isFormValid')) {
      var account = $('#account').val();
      var oldPwd = $('#oldPwd').val();
      var newPwd = $('#newPwd').val();
      var confirmPwd = $('#confirmPwd').val();

      $.post("/user/modifyPwd", {
        account : account,
        oldPwd : oldPwd,
        newPwd : newPwd,
        confirmPwd : confirmPwd
      }, function(data){
        $('#message').text(data.message);
        $('#result').modal({
          relatedTarget: this,
          onConfirm: function(options) {
            if (data.resultCode==0) {
              window.history.back();
            }
          }
        });
      });
    }
  });

  //收藏
  $('#addCollect').click(function(){
    var bookId = $(this).attr('bookId');
    $.post("addCollect",{
      bookId : bookId
    }, function(data){
      $('#message').text(data.message);
      if (data.resultCode==0) {
        $('#result').modal({
          relatedTarget: this,
          onConfirm: function(options) {
            window.location.href = "/book/myCollect";
          }
        });
      }else{
        $('#result').modal({
          relatedTarget: this,
          onConfirm: function(options) {
          }
        });
      }
    });
  });

  //搜索
  $('#search').click(function(){
    var key = $('#key').val();
    if (!key) {
      $('#message').text("请输入搜索关键字！");
      $('#result').modal();
    }else{
      $('#searchForm').submit();
    }
  });

  //添加书签
  $('#addBookmark').click(function(){
    var textNumber = $(this).attr("textNumber");
    var bookId = $(this).attr("bookId");
    var start = $(this).attr("start");

    $.post("/bookmark/addBookmark", {
      textNumber : textNumber,
      bookId : bookId,
      start : start
    }, function(data){
      $('#message').text(data.message);
      $('#delBookmark').attr('bookmarkId', data.bookmarkId);
      $('#delBookmark').css('display','');
      $('#addBookmark').css('display', 'none');
      $('#result').modal({
        relatedTarget: this,
        onConfirm: function(options) {
        }
      });
    });
  });

  //删除书签
  $('#delBookmark').click(function(){
    var bookmarkId = $(this).attr('bookmarkId');

    $.post("/bookmark/delBookmark", {
      bookmarkId : bookmarkId
    }, function(data){
      $('#message').text(data.message);
      $('#delBookmark').css('display', 'none');
      $('#addBookmark').css('display', '');
      $('#result').modal({
        relatedTarget: this,
        onConfirm: function(options) {
        }
      });
    });
  });

  //删除书签
  $('.toDelBookmark').click(function(){
    var bookmarkId = $(this).attr('bookmarkId');
    $('#delConfirm').modal({
      relatedTarget: this,
      onConfirm: function(options) {
        $.post("/bookmark/delBookmark",{
          bookmarkId : bookmarkId
        },function(data){
          $('#message').text(data.message);
          $('#result').modal({
            relatedTarget: this,
            onConfirm: function(options) {
              location.reload();
            }
          });
        });
      }
    });
  });
});

function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=")
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 
    c_end=document.cookie.indexOf(";",c_start)
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end))
    } 
  }
return ""
}
