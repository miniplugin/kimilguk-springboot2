var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function(){
            _this.save();
        })
        $('#btn-update').on('click', function(){
            _this.update();
        })
        $('#btn-delete').on('click', function(){
            _this.delete();
        })
    },
    saveFile : function() {
        var _this = this;
        var form = $('#form_posts')[0];
        var formData = new FormData(form);
        $.ajax({
            async: false,//게시물 등록시 첨부파일은 비동기에서 동기로 바꿔야지만, 업로드 후 게시물이 저장됩니다.
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/api/upload",
            data: formData,
            processData: false,//formData 를 QueryString 으로 변환하지 않는다.
            contentType: false,//multipart/form-data; boundary=----WebKitFormBoundaryzS65BXVvgmggfL5A
            cache: false,
            timeout: 600000,
            dataType: 'json',
            success: function (result) {
                alert("첨부파일 OK : " + result);
                $("#file_id").val(result);
            },
            //complete: _this.save,
            error: function (e) {
                $("#file_id").text("");
                console.log("ERROR : ", e);
                alert("첨부파일 업로드가 실패했습니다.");
                return;
            }
        });
    },
    save : function () {
        var _this = this;
        if($("#customFile").val() != "") {
            _this.saveFile();
            //alert($('#file_id').val());
        }
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val(),
            fileId: $('#file_id').val()
        };
        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            /*
            beforeSend : function(xhr) {
                if (false) {
                    xhr.abort();
                }
            },
            */
            data: JSON.stringify(data)
        }).done(function(result){
            alert('글이 등록되었습니다.' + result);
            window.location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };
        var id = $('#id').val();
        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(){
            alert('글이 수정되었습니다.');
            window.location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(){
            alert('글이 삭제되었습니다.');
            window.location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }
};

main.init()