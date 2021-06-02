///////////////open sidebar///////////////////
      var menu_btn = document.querySelector("#menu-btn");
      var sidebar = document.querySelector("#sidebar");
      var container = document.querySelector(".my-container");
      menu_btn.addEventListener("click", () => {
        sidebar.classList.toggle("active-nav");
        container.classList.toggle("active-cont");
      });
//////////////////////////////////////////////
var filterValue = "now";
var komplex_id = 0;
function filter(value){
document.getElementById("komplex").value="";
filterValue = value;
    $.ajax({
    	type: 'get',
    	url: '/userPage/purchasing-table/filter/' + value,
    	success: function(data) {
    		$('.table-purchasing').html(data);
    		komplex_id = 0;
    	},
    });
}
//////////////////////////////////////////////

function filterByKomplex(value){
komplex_id = value;
    $.ajax({
        type: 'get',
        url: '/userPage/purchasing-table/by-komplex/'+filterValue+'/'+value,
        success: function(data) {
        	$('.table-purchasing').html(data);
        },
    });
}
///////////////////////////////////////////////
function print(){
    $.ajax({
        type : "GET",
        contentType : "application/json",
        accept: 'text/plain',
        url: '/userPage/purchasing-table/print-table/'+filterValue+'/'+komplex_id,
        dataType: 'binary',
        xhrFields: {
             'responseType':'blob'
        },
        success: function(data,status,xhr) {
                 var blob = new Blob([data],{type: xhr.getResponseHeader('Content-Type')});
                 var link = document.createElement('a');
                 link.href = window.URL.createObjectURL(blob);
                 var today = formatDateToRus(new Date());
                 var komplexName = "общая"
                 if ($("#komplex option:selected").val() !== ""){
                    komplexName = $("#komplex option:selected").text();
                 }
                 link.download = 'Закупочная таблица СИЗ от '+today+' '+komplexName+'.xlsx';
                 link.click();
        },
        error : function(e) {
                 alert("Error!")
                 console.log("ERROR: ", e);
        }
    });
}
function formatDateToRus(dateTime){
        var dd = String(dateTime.getDate()).padStart(2, '0');
        var mm = String(dateTime.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = dateTime.getFullYear();

        return dd + '.' + mm + '.' + yyyy;
}