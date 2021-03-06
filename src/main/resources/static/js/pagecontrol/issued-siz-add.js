var extendingSIZId;
var writeoffSIZId;
//// modal functions///////
function showModalWindow(value) {
	extendingSIZId = value;
	$("#exampleModalLive").modal('show');
}
function showModalWindow2(value) {
	writeoffSIZId = value;
	$("#exampleModalLive2").modal('show');
}
function showModalWindow3() {
	$("#exampleModalLive3").modal('show');
}
function closeModalWindow() {
	$("#exampleModalLive").modal('hide');
	$("#exampleModalLive2").modal('hide');
	$("#exampleModalLive3").modal('hide');
};
///////////////////////////////////
var employee_id;
var selRecs = [];

function loadEmployeesForSelectedKomplex() {
if (document.getElementById("post").value !==''){
    loadEmployeesForSelectedKomplexAndPost();
} else {
	var e = document.getElementById("komplex");
	var komplexId = e.value;
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/getEmployeeForKomplex/' + komplexId,
		success: function(data) {
			$('.table-employees').html(data);
		},
	})
	}
	selRecs = [];
};
///////////////////////////////////////////////
function loadEmployeesForSelectedPost() {
	var element = document.getElementById("komplex");
	if (element !== null) {
		if (document.getElementById("komplex").value !== '') {
			loadEmployeesForSelectedKomplexAndPost();
		} else {
			var e = document.getElementById("post");
			var postId = e.value;
			$.ajax({
				type: 'get',
				url: '/userPage/issued-siz/getEmployeeForPost/' + postId,
				success: function (data) {
					$('.table-employees').html(data);
					$('body input:checkbox').prop('disabled', false);
				},
			})
			updateSizForPost();
		}
	} else {
		var e = document.getElementById("post");
		var postId = e.value;
		$.ajax({
			type: 'get',
			url: '/userPage/issued-siz/getEmployeeForPost/' + postId,
			success: function (data) {
				$('.table-employees').html(data);
				$('body input:checkbox').prop('disabled', false);
			},
		})
		updateSizForPost();
	}
	selRecs = [];
};
///////////////////////////////////////////////
function loadEmployeesForSelectedKomplexAndPost() {
	var a = document.getElementById("komplex");
	var b = document.getElementById("post");
	var komplexId = a.value;
	var postId = b.value;
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/getEmployeeForKomplexAndPost/' + komplexId+'/'+postId,
		success: function(data) {
			$('.table-employees').html(data);
			$('body input:checkbox').prop('disabled',false);
		},
	})
	updateSizForPost();
	selRecs = [];
};
///////////////////////////////////////////////
function rowClicked(empl_id) {
	employee_id = empl_id;
	updateSizForEmployee(empl_id);
	loadIssuedSiz(empl_id);
};

function updateSizForPost(){
$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/getSizForPost/' + document.getElementById("post").value,
		success: function(data) {
			$('.table-siz').html(data);
		},
	})
};

function updateSizForEmployee(value) {
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/getSizForEmployee/' + value,
		success: function(data) {
			$('.table-siz').html(data);
			if(selRecs.length !== 0){
	            document.getElementsByName('iss-btn').forEach(el => el.disabled = false);
	        }
		},
	})
};

function loadIssuedSiz(value) {
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/getIssuedSizForEmployee/' + value,
		success: function(data) {
			$('.table-issuedSiz').html(data);
		},
	})
};
//////////////////////////////////////////
function updateIssuedSiz(value) {

    $.ajax({
	    type: 'get',
	    url: '/userPage/issued-siz/' + selRecs + '/add/'+value,
	    success: function(data) {
		    $('.table-issuedSiz').html(data);
		    document.getElementsByName('iss-btn').forEach(el => el.disabled = true);
            $('body input:checkbox').prop('checked',false);
            loadErrors();
		    updateSizForEmployee(employee_id);
		    printIssuedSIZ();
	},
})
//$('body input:checkbox').prop('checked',false);
};
//////////////////////////////////////////
function printIssuedSIZ(){
    $.ajax({
    	type : "POST",
    	contentType : "application/json",
    	accept: 'text/plain',
    	url : '/userPage/issued-siz/print-issued-siz',
    	dataType: 'binary',
    	xhrFields: {
    	    'responseType':'blob'
    	},
    	success : function(data,status,xhr) {
    	    var blob = new Blob([data],{type: xhr.getResponseHeader('Content-Type')});
    	    var link = document.createElement('a');
    	    link.href = window.URL.createObjectURL(blob);
    	    var today = formatDateToRus(new Date());
    	    link.download = 'Акт выдачи СИЗ сотрудникам от '+today+'.xlsx';
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
//////////////////////////////////////////
function loadErrors(){
        $.ajax({
	    	type: 'get',
	    	url: '/userPage/issued-siz/errors/',
	    	success: function(data) {
	    		$('.table-errors').html(data);
                var count = $('#tb_err tr').length - 1;
                if (count>0){
                    alert('Возникли некоторые проблемы при выдачи СИЗ сотрудникам');
                }
	    	},
	    })
}
//////////////////////////////////////////
var dateExtending;

function setDateExtending(value) {
	dateExtending = value;
};

function extendSIZ() {
	closeModalWindow();
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/' + extendingSIZId + '/extend/' + dateExtending,
		success: function(data) {
			$('.table-issuedSiz').html(data);
		},
	})
};
//////////////////////////////////
var actName;

function setActWriteoff(value){
    actName = value;
}


//////////////////////////////////
function cancelIssed(value) {
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/' + value + '/cancel',
		success: function(data) {
			$('.table-issuedSiz').html(data);
		},
	});
	updateSizForEmployee(employee_id);
};
//////////////////////////////////
function writeoffSIZ() {
	closeModalWindow();
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/' + writeoffSIZId + '/writeoff/'+actName,
		success: function(data) {
			$('.table-issuedSiz').html(data);
		},
	});
};
//////////////////////////////////

function searchingByEmployee(value) {
	if (value.length === 0) {
		value = 0;
	}
	$.ajax({
		type: 'get',
		url: '/userPage/issued-siz/search/employee/' + value,
		success: function(data) {
			$('.table-employees').html(data);
		},
	});
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


function selectRecord(value, id,post_id) {
    rowClicked(id)
	if (value.checked) {
		selRecs.push(Number(id));
	} else {
		selRecs = arrayRemove(selRecs, Number(id));
	}
	var btns =  document.getElementsByName('iss-btn');
	if(selRecs.length === 0){
	  btns.forEach(el => el.disabled = true);
	} else {
	  btns.forEach(el => el.disabled = false);
	}
};

function arrayRemove(arr, value) {
	return arr.filter(function(ele) {
		return ele !== value;
	});
}
////////////////////////////////////
function selectAllRecord(value){
    if (value.checked) {
 	    var list = [];
        document.getElementsByName('id-empl').forEach(el => list.push(Number(el.innerText)));
        selRecs = list;
        rowClicked(selRecs[0]);
	} else {
		selRecs = [];
	}
	$('body input:checkbox').prop('checked',value.checked);
	var btns =  document.getElementsByName('iss-btn');
	if ((selRecs.length === 0)||(selRecs === null)){
	    btns.forEach(el => el.disabled = true);
	}else {
		btns.forEach(el => el.disabled = false);
	}
}
//////////////////////////////////////////////////

function getParentTag(node, tag) {
	if (node) {
		return (node.tagName == tag) ? node : getParentTag(node.parentElement, tag);
	}
	return null;
};
var tb = document.getElementById('tb');
tb.setAttribute('activeRowIndex', 0);
tb.addEventListener('click', cli);

function cli(e) {
	var row = getParentTag(e.target, 'TR');
	if ((!row) || (row.rowIndex == 0)) {
		return;
	}
	var tbl = this,
		idx = tbl.getAttribute('activeRowIndex');
	tbl.rows[idx].classList.remove('activeRow');
	row.classList.add('activeRow');
	tbl.setAttribute('activeRowIndex', row.rowIndex);
};