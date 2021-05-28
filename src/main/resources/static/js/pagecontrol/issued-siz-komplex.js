function filterByKomplex(value){
    $.ajax({
    		type: 'get',
    		url: '/userPage/issued-for-komplex/get/' + value,
    		success: function(data) {
    			$('.table-siz').html(data);
    			loadClickListener();
    		},
    	})
}

/////////////////////////////////////////////
function plusNumber(value){
    console.log()
}
///////////////open sidebar///////////////////

      var menu_btn = document.querySelector("#menu-btn");
      var sidebar = document.querySelector("#sidebar");
      var container = document.querySelector(".my-container");
      menu_btn.addEventListener("click", () => {
        sidebar.classList.toggle("active-nav");
        container.classList.toggle("active-cont");
      });
//////////////////////////////////////////////
function loadClickListener(){
     const items = document.querySelectorAll('#tb tbody tr');
     Array.from(items).forEach((item) => {
         const btnMinus = item.querySelector('[data-type="minus"]');
         const btnPlus = item.querySelector('[data-type="plus"]');
         const input = item.querySelector('input');
         btnMinus.addEventListener('click', () => {
         btnPlus.disabled = false;
             if (Number(input.value)===0){
                 btnMinus.disabled = true;
             }else{
                 input.value = Number(input.value) - 1;
             }
         });
         btnPlus.addEventListener('click', () => {
             btnMinus.disabled = false;
             if (input.value === input.max){
                btnPlus.disabled = true;
             }else{
                input.value = Number(input.value) + 1;
             }
         });
     });
}
////////////////collect data and send////////////

function sendToKomplex(){
    var listOfObjects = [];
    const items = document.querySelectorAll('#tb tbody tr');
    Array.from(items).forEach((item) => {
       var SIZForPurchase = {
            id: Number(item.querySelector('[data-type="id"]').innerHTML),
            namesiz: item.querySelector('[data-type="namesiz"]').innerHTML,
            height: item.querySelector('[data-type="height"]').innerHTML,
            size: item.querySelector('[data-type="size"]').innerHTML,
            number: item.querySelector('input').value
       }
       listOfObjects.push(SIZForPurchase);
    });

    $.ajax({
    	type: 'post',
    	url: '/userPage/issued-for-komplex/send/' + listOfObjects,
    	success: function(data) {
    		$('.info').html(data);
    	},
    })
}