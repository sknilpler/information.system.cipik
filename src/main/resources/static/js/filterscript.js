<script th:inline="javascript">

/*<1[CDATA[*/
var listdata = /*[[${listdata}]]*/;
/*]]>*/

$('#textSearch').on('keyup', function(){
  var value = $(this).val();

  var data = FilterFunction(value, listdata);

  rebuildTable(data)
});

function FilterFunction(value,data){
  var filteredData = [];
  for(var i = 0; i < data.length; i++){
    value = value.toLowerCase();
    var column = data[i].searchColumn.toLowerCase();

    if(column.includes(value)){
      filteredData.push(data[i]);
    }
  }
  return filteredData;
}


function rebuildTable(data){
  var table = document.getElementById('exampleTable')
  table.innerHTML=''
  for(var i = 0; i<data.length; i++){
    var row =  `<tr>
		    <td>${data[i].column1}</td>
		    <td>${data[i].column2}</td>
		    <td>${data[i].column3}</td>
		</tr>`
		table.innerHTML+=row
  }
}
</script>