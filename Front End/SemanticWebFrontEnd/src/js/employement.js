function queryForLowEmployementRateInTheGivenTown(town, year, cb)
{
  $.post("http://ec2-54-149-233-81.us-west-2.compute.amazonaws.com:3030/ds/query", {
   query: "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \
PREFIX townuri: <http://connecticutsemwebtowndata.com/employementdata#> \
PREFIX data: <http://www.semanticweb.org/satyam/ontologies/2016/9/untitled-ontology-9#> \
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \
SELECT ?ptype ?erate  \
WHERE {\
  ?x a ?town. \
  ?x data:townName ?tname \
  FILTER (regex(str(?tname), \"" + town + "\")). \
	?x data:professiontype ?ptype; \
			data:employmentrate ?erate; \
   			data:year ?yr \
  FILTER (?yr = \""+ year + "\"). \
}\
ORDER BY DESC (xsd:integer(?erate)) \
LIMIT 5"
 },
 function(data, status) {
   var headers = [];
   var values = [[], []];

   data.head.vars.forEach(function(item) {
     headers.push(item);
   });

   data.results.bindings.forEach(function(item) {
       values[0].push(item[headers[0]].value);
       values[1].push(item[headers[1]].value);
   });

   cb(values);
 });

}

function queryForEmployementRateInTheGivenTownAndJobType(town, year, professionlist, cb)
{
  var q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\
PREFIX townuri: <http://connecticutsemwebtowndata.com/employementdata#>\
PREFIX data: <http://www.semanticweb.org/satyam/ontologies/2016/9/untitled-ontology-9#>\
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\
SELECT DISTINCT ?ptype ?erate \
WHERE {\
  ?x a ?town.\
  ?x data:townName ?tname \
  FILTER (regex(str(?tname), \"" + town +"\")). \
	?x data:professiontype ?ptype; \
			data:employmentrate ?erate; \
   			data:year ?yr \
  FILTER (?yr = \""+ year + "\" && (" + professionlist + ") ). \
}";
$.post("http://ec2-54-149-233-81.us-west-2.compute.amazonaws.com:3030/ds/query", {
 query: q
},
function(data, status) {
  var headers = [];
  var values = [[], []];
  data.head.vars.forEach(function(item) {
    headers.push(item);
  });

  data.results.bindings.forEach(function(item) {
      values[0].push(item[headers[0]].value);//ptype
      values[1].push(item[headers[1]].value);//erate
  });

  cb(values);
});
}
