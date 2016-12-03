function tableCreate(data) {
  table = document.createElement('table'),
    thead = document.createElement('thead'),
    tbody = document.createElement('tbody'),
    tr = document.createElement('tr');
  var headers = [];
  $(table).addClass("table table-hover");


  data.head.vars.forEach(function(item) {
    var th = document.createElement('th');
    $(th).attr("data-field", item);
    th.innerHTML = item;
    tr.appendChild(th);
    headers.push(item);
  });


  thead.appendChild(tr);
  table.appendChild(thead);

  data.results.bindings.forEach(function(item) {
    tr1 = document.createElement('tr');

    for (var i = 0; i < headers.length; i++) {
      var td = document.createElement('td');
      td.innerHTML = item[headers[i]].value;
      tr1.appendChild(td);

    }
    tbody.appendChild(tr1);
  });

  table.appendChild(tbody);

  $(table).addClass('table-striped');
  return table;
}

function tableCreateFromList(data) {
  var professions = [];
  var start=2012;
  $('#prabhanjan').empty();
  var table = document.createElement('table'),
    // thead = document.createElement('thead'),
    tbody = document.createElement('tbody'),
    tr = document.createElement('tr');
  var headers = [];
  $(table).addClass("table table-hover");
  var td = document.createElement('td');
  td.innerHTML = "year \\ professions";
  tr.appendChild(td);

  data[0][0].forEach(function(item) {
    professions.push(item);
    td = document.createElement('td');
    td.innerHTML = item;
    tr.appendChild(td);
  });

tbody.appendChild(tr);
for (var index = 0 ; index < data.length; index++){
  var tr1 = document.createElement('tr');
  td = document.createElement('td');
  td.innerHTML = start;
  tr1.appendChild(td);
  for (var j =0; j< 5; j++){
    var td1 = document.createElement('td');
    td1.innerHTML = data[index][1][j];
    tr1.appendChild(td1);
  }
  start = start + 1;
  tbody.appendChild(tr1);
}

table.appendChild(tbody);
$('#prabhanjan').append(table);
}
