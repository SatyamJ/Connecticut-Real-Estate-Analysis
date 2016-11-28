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
  return table;
}
