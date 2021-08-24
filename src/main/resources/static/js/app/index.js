var main = {
    init : function () {
        var _this = this;
        $('#btn-delete-many-file').on('click', function(){
            _this.deleteManyFile();
            location.reload(true);
        })
        $('#btn-delete-file').on('click', function(){
            _this.deleteFile();
            location.reload(true);
        })
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
    deleteManyFile : function() {
        var fileId = $('#many_file_id').val();
        $.ajax({
            async: false,//게시물 등록시 첨부파일은 비동기에서 동기로 바꿔야지만, 업로드 후 게시물이 저장됩니다.
            type: 'DELETE',
            url: '/api/many_file_delete/' + fileId,
            //dataType: 'text',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(result){
            $("#many_file_id").val("");
            alert('첨부파일 삭제 성공 ' +  result.success);
        }).fail(function(error){
            alert('첨부파일 삭제 실패 ' + JSON.stringify(error));
        });
    },
    saveManyFile : function(post_id) {
        var _this = this;
        var form = $('#form_posts')[0];
        var formData = new FormData(form);
        formData.append('post_id', post_id);
        $.ajax({
            async: false,//게시물 등록시 첨부파일은 비동기에서 동기로 바꿔야지만, 업로드 후 게시물이 저장됩니다.
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/api/many_file_upload",
            data: formData,
            processData: false,//formData 를 QueryString 으로 변환하지 않는다.
            contentType: false,//multipart/form-data; boundary=----WebKitFormBoundaryzS65BXVvgmggfL5A
            cache: false,
            timeout: 600000,
            dataType: 'json',
            beforeSend:function(){
                //이미지 보여주기 처리
                $('.wrap-loading').removeClass('display-none');
            },
            complete:function(){
                //이미지 숨김 처리
                $('.wrap-loading').removeClass('display-none');
            },
            success: function (result) {
                alert("첨부파일 OK : " + result);
                $("#many_file_id").val(result);
            },
            //complete: _this.save,
            error: function (e) {
                $("#many_file_id").val("");
                console.log("ERROR : ", e);
                alert("첨부파일 업로드가 실패했습니다.");
                return;
            }
        });
    },
    deleteFile : function() {
        var fileId = $('#file_id').val();
        $.ajax({
            async: false,//게시물 등록시 첨부파일은 비동기에서 동기로 바꿔야지만, 업로드 후 게시물이 저장됩니다.
            type: 'DELETE',
            url: '/api/file_delete/' + fileId,
            //dataType: 'text',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function(result){
            $("#file_id").val("");
            alert('첨부파일 삭제 성공 ' +  result.success);
        }).fail(function(error){
            alert('첨부파일 삭제 실패 ' + JSON.stringify(error));
        });
    },
    saveFile : function() {
        var _this = this;
        var form = $('#form_posts')[0];
        var formData = new FormData(form);
        $.ajax({
            async: false,//게시물 등록시 첨부파일은 비동기에서 동기로 바꿔야지만, 업로드 후 게시물이 저장됩니다.
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/api/file_upload",
            data: formData,
            processData: false,//formData 를 QueryString 으로 변환하지 않는다.
            contentType: false,//multipart/form-data; boundary=----WebKitFormBoundaryzS65BXVvgmggfL5A
            cache: false,
            timeout: 600000,
            dataType: 'json',
            beforeSend:function(){
                //이미지 보여주기 처리
                $('.wrap-loading').removeClass('display-none');
            },
            complete:function(){
                //이미지 숨김 처리
                $('.wrap-loading').removeClass('display-none');
            },
            success: function (result) {
                alert("첨부파일 OK : " + result);
                $("#file_id").val(result);
            },
            //complete: _this.save,
            error: function (e) {
                $("#file_id").val("");
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
            if($("#manyFile").val() != "") {
                //alert($('#many_file_id').val());
                _this.saveManyFile(result);
            }
            alert('글이 등록되었습니다.' + result);
            window.location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var _this = this;
        var id = $('#id').val();
        if($("#customFile").val() != "") {
            if($("#file_id").val() != "") {// && $("#file_id").val() != undefined) {
                _this.deleteFile();
            }
            _this.saveFile();
        }
        if($("#manyFile").val() != "") {
            if($("#many_file_id").val() != "") {// && $("#file_id").val() != undefined) {
                _this.deleteManyFile();
            }
            _this.saveManyFile(id);
        }
        var data = {
            title: $('#title').val(),
            content: $('#content').val(),
            fileId: $('#file_id').val()
        };
        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(){
            alert('글이 수정되었습니다.');
            //window.location.href = "/";
            location.reload();
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        if(!confirm("정말로 삭제 하시겠습니까?")) {
            return;
        }
        var _this = this;
        if($("#file_id").val() != "") {// && $("#file_id").val() != undefined) {
            _this.deleteFile();
        }
        if($("#many_file_id").val() != "") {// && $("#file_id").val() != undefined) {
            _this.deleteManyFile();
        }
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
