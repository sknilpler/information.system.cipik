function selectOtdel2() {
var e = document.getElementById("otdel");
var strOtdelId = e.value;
console.log(strOtdelId);

   var xhr = new XMLHttpRequest();
   xhr.withCredentials = true;
   xhr.addEventListener("readystatechange", function() {
      if(this.readyState === 4) {
        employees = JSON.parse(this.responseText);
        console.log(employees);

      }
   });

    xhr.open("GET", "/userPage/issued-siz/getEmployeeForOtdel/"+strOtdelId,false);
    xhr.send();
};