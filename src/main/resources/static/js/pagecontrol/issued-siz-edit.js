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
function closeModalWindow() {
	$("#exampleModalLive").modal('hide');
	$("#exampleModalLive2").modal('hide');
    $("#exampleModalLive3").modal('hide');
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

function setDateExtending(value) {
	dateExtending = value;
};

function extendSIZ() {
	closeModalWindow();
	$.ajax({
		type: 'get',
		url: '/userPage/employee-siz/edit-staffing/' + extendingSIZId + '/extend/' + dateExtending,
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
		url: '/userPage/employee-siz/edit-staffing/' + value + '/cancel',
		success: function(data) {
			$('.table-issuedSiz').html(data);
			updateSizForEmployee();
		},
	});
};
//////////////////////////////////
function writeoffSIZ() {
	closeModalWindow();
	$.ajax({
		type: 'get',
		url: '/userPage/employee-siz/edit-staffing/' + writeoffSIZId + '/writeoff/'+actName,
		success: function(data) {
			$('.table-issuedSiz').html(data);
		},
	});
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