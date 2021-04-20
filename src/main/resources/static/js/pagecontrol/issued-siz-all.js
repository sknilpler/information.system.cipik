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
function searching(value) {
	if (value.length === 0) {
		value = 0;
	}
	$.ajax({
		type: 'get',
		url: '/userPage/employee-siz/search/employee/' + value,
		success: function(data) {
			$('.table-employees').html(data);
		},
	});
};
//////////////////////////////////////////////
function filter(value){
    document.getElementById("textSearch").innerHTML="";
    $.ajax({
    	type: 'get',
    	url: '/userPage/employee-siz/filter/employee/' + value,
    	success: function(data) {
    		$('.table-employees').html(data);
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

