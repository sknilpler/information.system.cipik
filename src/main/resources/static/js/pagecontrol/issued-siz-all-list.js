function closeModalWindow() {
	$("#exampleModalLive").modal('hide');
	$("#exampleModalLive2").modal('hide');
    $("#exampleModalLive3").modal('hide');
    $("#historyModalLive3").modal('hide');
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

/////////////////////////////////////////////
//// modal functions///////
function showModalWindow(value) {
	extendingSIZId = value;
	$("#exampleModalLive").modal('show');
}
function showModalWindow2(value) {
	writeoffSIZId = value;
	$("#exampleModalLive2").modal('show');
}
function showModalWindow3(value) {
	$.ajax({
            type: 'get',
            url: '/userPage/employee-siz/edit-staffing/employee/history-issued-siz/' + value,
            success: function(data) {
            	$('.table-history-issued-siz').html(data);
            	$("#exampleModalLive3").modal('show');
            },
        });
}
//////////////////////////////////////////
var dateExtending;
var protocol;
var protocolDate;

function setDateExtending(value) {
	dateExtending = value;
};

function setProtocolExtending(value){
    protocol = value;
}

function setDateProtocol(value){
    protocolDate = value;
}

function extendSIZ() {
    if ((dateExtending != '') && (dateExtending != undefined) && (protocol != '') && (protocol != undefined) &&
            (protocolDate != '') && (protocolDate != undefined)){
	    closeModalWindow();
	    $.ajax({
		    type: 'get',
		    url: '/userPage/employee-siz/edit-staffing/' + extendingSIZId + '/extend/' + dateExtending + '/protocol/' + protocol + '/date/' + protocolDate,
		    success: function(data) {
			   location.reload();
		    },
	    })
	} else alert("Не все данные введены");
};
//////////////////////////////////
var actName;

function setActWriteoff(value){
    actName = value;
}
//////////////////////////////////
function writeoffSIZ() {
  if ((actName != '') && (actName != undefined)){
	    closeModalWindow();
	    $.ajax({
		    type: 'get',
		    url: '/userPage/employee-siz/edit-staffing/' + writeoffSIZId + '/writeoff/'+actName,
		    success: function(data) {
			   location.reload();
		    },
	    });
	} else alert("Не все данные введены");
};
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
function ExportToExcel(type, fn, dl) {
       var elt = document.getElementById('tb');
       var wb = XLSX.utils.table_to_book(elt, { sheet: "Список СИЗ" });
       return dl ?
         XLSX.write(wb, { bookType: type, bookSST: false, type: 'base64', raw: true }):
         XLSX.writeFile(wb, fn || ('Список выданного СИЗ.' + (type || 'xlsx')));
}

function hideCol(table,colIndex){
      var elt = document.getElementById('tb');
        for (var i = 0, col; col = elt.cols[i]; i++) {

           if(colIndex==i){
              col.className ="hiddenclass";
           }
          }

      }

function print(){
    var komplexSelector = document.getElementById("issued-by-komplex");
    var sortSelector = document.getElementById("issued-sort");
    var textSearch = document.getElementById("textSearch");
    if (komplexSelector !== null){
        id_komplex = komplexSelector.value;
    }
    if (id_komplex === 'all' || id_komplex === '') {
        id_komplex = 0;
    }
    var sort_type = 'none';
    if (sortSelector !== null){
        sort_type = sortSelector.value;
    }
    if (sort_type === '') {
        sort_type = 'none';
    }
    var keyword = 'none';
    if (textSearch !== null){
        keyword = textSearch.value;
    }
    if (keyword === '') {
        keyword = 'none';
    }
    console.log(id_komplex);
    console.log(sort_type);
    console.log(keyword);
    $.ajax({
        type : "GET",
        contentType : "application/json",
        accept: 'text/plain',
        url: '/userPage/list-issued-siz/print-table/'+id_komplex + '/'+sort_type+'/'+keyword,
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
                 link.download = 'Выданный СИЗ сотрудникам'+komplexName+'.xlsx';
                 link.click();
        },
        error : function(e) {
                 alert("Error!")
                 console.log("ERROR: ", e);
        }
    });
}