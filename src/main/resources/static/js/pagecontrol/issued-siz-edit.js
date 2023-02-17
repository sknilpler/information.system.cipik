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

function editdateon(id) {
    selected_id = id;
    document.getElementById("dataOn").value = null;
    $("#editDateOn").modal('show');
};

function editdateoff(id) {
    selected_id = id;
    document.getElementById("dataOn").value = null;
    $("#editDateOff").modal('show');
};

function saveNewDateOn() {
    var d = document.getElementById("dataOn").value;
    if (d === '') {
        alert("Выберите дату!");
    } else {
    $.ajax({
    		type: 'get',
    		url: '/userPage/employee-siz/edit-staffing/employee/siz/'+selected_id +'/date-on/' + d,
    		success: function(data) {
    			$('.table-issuedSiz').html(data);
    		},
    	})
    	 $("#editDateOn").modal('hide');
    }
}

function saveNewDateOff() {
    var d = document.getElementById("dataOff").value;
    if (d === '') {
        alert("Выберите дату!");
    } else {
       $.ajax({
          		type: 'get',
          		url: '/userPage/employee-siz/edit-staffing/employee/siz/'+selected_id +'/date-off/' + d,
          		success: function(data) {
          			$('.table-issuedSiz').html(data);
          		},
          	})
          	 $("#editDateOff").modal('hide');
    }
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

function closeModalWindow2() {
	$("#protocolNameChangeModal").modal('hide');
	$("#protocolDateChangeModal").modal('hide');
    $("#protocolExtendingChangeModal").modal('hide');
};

function closeModalWindow() {
	$("#exampleModalLive").modal('hide');
	$("#exampleModalLive2").modal('hide');
    $("#exampleModalLive3").modal('hide');
    $("#historyModalLive3").modal('hide');
    $("#editDateOn").modal('hide');
    $("#editDateOff").modal('hide');
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
		    url: '/userPage/employee-siz/edit-staffing/' + extendingSIZId + '/extend/' + dateExtending + '/protocol/' + protocol.replace(/\/|\\/g , '@') + '/date/' + protocolDate,
		    success: function(data) {
			    $('.table-issuedSiz').html(data);
		    },
	    })
	} else alert("Не все данные введены");
};

////////////////////////////changing protocol info////////////
var protocol_id;
var newPName;
var newPDate;
var newPExtending;

function editprotocolname(value){
    protocol_id = value;
    $("#protocolNameChangeModal").modal('show');
}
function editdateprotocol(value){
    protocol_id = value;
    $("#protocolDateChangeModal").modal('show');
}
function editdateextending(value){
    protocol_id = value;
    $("#protocolExtendingChangeModal").modal('show');
}

function setNewProtocolName(value){
    newPName = value;
}
function setNewProtocolDate(value){
    newPDate = value;
}
function setNewProtocolExtending(value){
    newPExtending = value;
}

function saveNewProtocolName(){
     $.ajax({
    		    type: 'get',
    		    url: '/userPage/employee-siz/edit-protocol/' + protocol_id + '/name/' + newPName,
    		    success: function(data) {
    			    $('.table-history-extended-siz').html(data);
    			    closeModalWindow2();
    		    },
    	    })
}

function saveNewProtocolDate(){
     $.ajax({
    		    type: 'get',
    		    url: '/userPage/employee-siz/edit-protocol/' + protocol_id + '/date/' + newPDate,
    		    success: function(data) {
    			    $('.table-history-extended-siz').html(data);
    			    closeModalWindow2();
    		    },
    	    })
}

function saveNewProtocolExtnding(){
     $.ajax({
    		    type: 'get',
    		    url: '/userPage/employee-siz/edit-protocol/' + protocol_id + '/date-ext/' + newPExtending,
    		    success: function(data) {
    			    $('.table-history-extended-siz').html(data);
    			    closeModalWindow2();
    		    },
    	    })
}
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
		    url: '/userPage/employee-siz/edit-staffing/' + writeoffSIZId + '/writeoff/'+actName.replace(/\/|\\/g , '@'),
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

function post(path, method = 'post') {
    const form = document.createElement('form');
    form.method = method;
    form.action = path;
    const hiddenField = document.createElement('input');
    hiddenField.type = 'hidden';
    form.appendChild(hiddenField);
    document.body.appendChild(form);
    form.submit();
}