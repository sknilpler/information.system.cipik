function closeModalWindow() {
	$("#exampleModalLive").modal('hide');
	$("#exampleModalLive2").modal('hide');
};
///////////////open sidebar///////////////////
      var menu_btn = document.querySelector("#menu-btn");
      var sidebar = document.querySelector("#sidebar");
      var container = document.querySelector(".my-container");
      menu_btn.addEventListener("click", () => {
        sidebar.classList.toggle("active-nav");
        container.classList.toggle("active-cont");
      });
//////////////////////////////////////////////
var id_komplex = '';
//////////////////////////////////////////////
function searching(value) {
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
	if (value.length === 0) {
		value = 0;
	}
	$.ajax({
    	type: 'get',
    	url: '/userPage/list-issued-siz/filter/' + value+'/'+id_komplex,
    	success: function(data) {
    		$('.table-sizs').html(data);
    		document.getElementById("textSearch").innerHTML="";
		},
	});
};
//////////////////////////////////////////////
var filterValue;
function filter(value){
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    filterValue = value;
    document.getElementById("textSearch").innerHTML="";
    $.ajax({
    	type: 'get',
    	url: '/userPage/list-issued-siz/filter/' + value+'/'+id_komplex,
    	success: function(data) {
    		$('.table-sizs').html(data);
    	},
    });
}
//////////////////////////////////////////////
function showEmployeeWithEndDateWear(){
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    document.getElementById("textSearch").innerHTML="";
    $.ajax({
    	type: 'get',
    	url: '/userPage/list-issued-siz/show-issuedsiz-with-end-wear-date/'+id_komplex,
    	success: function(data) {
    		$('.table-sizs').html(data);
    	},
    });
}
function showEmployeeWithEndDateWearThisYear(){
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    document.getElementById("textSearch").innerHTML="";
    $.ajax({
    	type: 'get',
    	url: '/userPage/list-issued-siz/show-issuedsiz-with-end-wear-date-curr-year/'+id_komplex,
    	success: function(data) {
    		$('.table-sizs').html(data);
    	},
    });
}
function showEmployeeWithEndDateWearNextYear(){
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    document.getElementById("textSearch").innerHTML="";
    $.ajax({
    	type: 'get',
    	url: '/userPage/list-issued-siz/show-issuedsiz-with-end-wear-date-next-year/'+id_komplex,
    	success: function(data) {
    		$('.table-sizs').html(data);
    	},
    });
}
//////////////////////////////////////////////
function showModalWindow(value1,value2) {
   $.ajax({
       type: 'get',
       url: '/userPage/employee-siz/info-siz/employee/' + value1+'/'+value2,
       success: function(data) {
       	   $('.table-siz').html(data);
       	   $("#exampleModalLive").modal('show');
       },
   });
}
//////////////////////////////////////////////
function showModalWindow2(value) {
	$.ajax({
        type: 'get',
        url: '/userPage/employee-siz/info-issued-siz/employee/' + value,
        success: function(data) {
        	$('.table-issued-siz').html(data);
        	$("#exampleModalLive2").modal('show');
        },
    });
}
/////////////////////////////////////////////
function sorting(value){
document.getElementById("textSearch").innerHTML="";
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    if (value === 'date'){
        $.ajax({
            type: 'get',
            url: '/userPage/list-issued-siz/sorting-date/' + id_komplex,
            success: function(data) {
            	$('.table-sizs').html(data);
            },
        });
    }
    if (value === 'fio'){
        $.ajax({
            type: 'get',
            url: '/userPage/list-issued-siz/sorting-fio/' + id_komplex,
            success: function(data) {
            	$('.table-sizs').html(data);
            },
        });
    }
    if (value === 'tip'){
        $.ajax({
            type: 'get',
            url: '/userPage/list-issued-siz/sorting-tip/' + id_komplex,
            success: function(data) {
            	$('.table-sizs').html(data);
            },
        });
    }
    if (value === 'k_and_p'){
        $.ajax({
        	type: 'get',
        	url: '/userPage/list-issued-siz/sorting_k_p/' + id_komplex,
        	success: function(data) {
        		$('.table-sizs').html(data);
        	},
        });
    }
}
////////////////////////////
function filterByKomplex(value){

    if (value === 'all' || value === '') {
       id_komplex = 0;
    } else {
       id_komplex = value;
    }

    $.ajax({
     	type: 'get',
     	url: '/userPage/list-issued-siz/filter-by-komplex/'+id_komplex,
     	success: function(data) {
     		$('.table-sizs').html(data);
     		updateInfoWindow();
     	},
     });
}

function updateInfoWindow(){
  $.ajax({
  	type: 'get',
  	url: '/userPage/list-issued-siz/info-update/'+id_komplex,
  	success: function(data) {
  		$('.info-window').html(data);
  	},
  });
}
////////////////////////////////////////
function print(){
    var komplexSelector = document.getElementById("issued-by-komplex");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    $.ajax({
        type : "GET",
        contentType : "application/json",
        accept: 'text/plain',
        url: '/userPage/list-issued-siz/print-table/'+id_komplex,
        dataType: 'binary',
        xhrFields: {
             'responseType':'blob'
        },
        success: function(data,status,xhr) {
                 var blob = new Blob([data],{type: xhr.getResponseHeader('Content-Type')});
                 var link = document.createElement('a');
                 link.href = window.URL.createObjectURL(blob);
                 var komplexName = '';
                 if (($("#issued-by-komplex option:selected").val() !== "") && ($("#issued-by-komplex option:selected").val() !== "all")){
                    komplexName = ' '+$("#issued-by-komplex option:selected").text();
                 }
                 link.download = 'Укомплектованность сотрудников'+komplexName+'.xlsx';
                 link.click();
        },
        error : function(e) {
                 alert("Error!")
                 console.log("ERROR: ", e);
        }
    });
}