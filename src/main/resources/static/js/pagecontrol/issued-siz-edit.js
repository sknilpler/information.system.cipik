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

function showHistoryWindow(value) {
	$.ajax({
            type: 'get',
            url: '/userPage/employee-siz/edit-staffing/employee/history-extended-siz/' + value,
            success: function(data) {
            	$('.table-history-extended-siz').html(data);
            	$("#historyModalLive3").modal('show');
            },
        });
}

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
			    $('.table-issuedSiz').html(data);
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
function cancelIssed(value) {
	$.ajax({
		type: 'get',
		url: '/userPage/employee-siz/edit-staffing/' + value + '/cancel',
		success: function(data) {
			$('.table-issuedSiz').html(data);
			updateSizForEmployee();
		},
	});
};
//////////////////////////////////
function writeoffSIZ() {
  if ((actName != '') && (actName != undefined)){
	    closeModalWindow();
	    $.ajax({
		    type: 'get',
		    url: '/userPage/employee-siz/edit-staffing/' + writeoffSIZId + '/writeoff/'+actName,
		    success: function(data) {
			    $('.table-issuedSiz').html(data);
		    },
	    });
	} else alert("Не все данные введены");
};
//////////////////////////////////
function updateIssuedSiz(value) {
	$.ajax({
		type: 'get',
		url: '/userPage/employee-siz/edit-staffing/employee/add/'+value,
		success: function(data) {
			$('.table-issuedSiz').html(data);
		},
	})
	updateSizForEmployee();
	//$('body input:checkbox').prop('checked',false);

};
//////////////////////////////////////////
function updateSizForEmployee() {
	$.ajax({
		type: 'get',
		url: '/userPage/employee-siz/edit-staffing/employee/update-norms',
		success: function(data) {
			$('.table-siz').html(data);
		},
	})
};