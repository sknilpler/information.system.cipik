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
function filter(value){
document.getElementById("komplex").value="";
filterValue = value;
    $.ajax({
    	type: 'get',
    	url: '/userPage/purchasing-table/filter/' + value,
    	success: function(data) {
    		$('.table-purchasing').html(data);
    	},
    });
}
//////////////////////////////////////////////
function filterByKomplex(value){
    $.ajax({
        type: 'get',
        url: '/userPage/purchasing-table/by-komplex/'+filterValue+'/'+value,
        success: function(data) {
        	$('.table-purchasing').html(data);
        },
    });
}