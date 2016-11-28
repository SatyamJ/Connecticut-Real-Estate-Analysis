//Data

          //Angular App Module and Controller
          var sampleApp = angular.module('mapsApp', []);
          sampleApp.controller('MapCtrl', function ($scope) {
            var bounds = new google.maps.LatLngBounds();
                          $scope.townList = [
                            "Avon", "Beacon Falls", "Washington", "Griswold", "Eastford", "Enfield", "Orange", "Groton", "Bolton", "Hebron", "Roxbury",
                            "East Lyme", "Winchester", "Windham", "Weston", "Norwalk", "Glastonbury", "Killingly", "Chester", "Brookfield", "East Haddam",
                            "New Haven", "Milford", "Oxford", "Canton", "Coventry", "Newtown", "Lisbon", "Preston", "Scotland", "Southington", "Bethel",
                            "Bristol", "Pomfret", "Canterbury", "Haddam", "Monroe", "Cheshire", "Greenwich", "Westport", "Plainville", "Guilford", "Middlebury",
                            "Berlin", "West Hartford", "Shelton", "Morris", "Wilton", "Prospect", "Harwinton", "Stratford", "Salem", "Goshen", "Bridgewater",
                            "Colchester", "South Windsor", "East Windsor", "Bethlehem", "Vernon", "Madison", "Montville", "Marlborough", "Farmington", "Stafford",
                            "Manchester", "Tolland", "Thompson", "Torrington", "North Stonington", "Old Lyme", "Bridgeport", "Portland", "Fairfield",
                            "Wallingford", "Stamford", "Ridgefield", "East Haven", "Brooklyn", "New Canaan", "Voluntown", "Middletown", "Sterling",
                            "Old Saybrook", "Stonington", "Westbrook", "Lebanon", "Norfolk", "Ledyard", "Colebrook", "Waterbury", "Ashford", "Hamden",
                            "New Milford", "Litchfield", "Kent", "Darien", "Windsor Locks", "East Hampton", "Hartland", "Trumbull", "Easton", "Lyme",
                            "Putnam", "Norwich", "Newington", "Danbury", "Plymouth", "Union", "Windsor", "East Granby", "Barkhamsted", "Granby", "Woodstock",
                            "Meriden", "Naugatuck", "Middlefield", "Burlington", "Waterford", "Durham", "Watertown", "Hampton", "Suffield", "Hartford", "Wolcott",
                            "Willington", "Sherman", "Bethany", "Southbury", "Derby", "Ellington", "Cornwall", "Warren", "Branford", "Clinton", "Woodbridge",
                            "North Canaan", "Bloomfield", "Somers", "Bozrah", "North Branford", "New Fairfield", "Woodbury", "New Britain", "Deep River",
                            "Wethersfield", "Seymour", "Franklin", "Chaplin", "North Haven", "Mansfield", "Thomaston", "New Hartford", "Redding", "Canaan",
                            "Andover", "New London", "West Haven", "Sprague", "Salisbury", "Simsbury", "Rocky Hill", "East Hartford", "Plainfield", "Cromwell",
                            "Killingworth", "Essex", "Columbia", "Sharon", "Ansonia"
                          ];

                          $scope.complete = function(string){
                               $scope.hidethis = false;
                               var output = [];
                               angular.forEach($scope.townList, function(town){
                                    if(town.toLowerCase().indexOf(string.toLowerCase()) >= 0)
                                    {
                                         output.push(town);
                                    }
                               });
                               $scope.filterTown = output;
                          }

                          $scope.fillTextbox = function(string){
                               $scope.hidethis = true;
                               $scope.town = string;

                               $.post("http://localhost:3030/ds/query",
                                     {
                                       query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/>\
                                        PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#>\
                                        select DISTINCT ?name ?total_houses ?occupied_houses ?vacant_houses\
                                                         ?total_pop_2015 ?total_male_2015 ?total_female_2015\
                                                         ?address ?list_year ?assessed_value ?sales_price ?sales_ratio ?lat ?long where {\
                                                         ?ns1 ds:municipality ?name ;\
                                                              ds:total_housing_units ?total_houses ;\
                                                              ds:occupied_housing_units ?occupied_houses ;\
                                                              ds:vacant_housing_units ?vacant_houses .\
                                                         ?ns2 ds:town ?name ;\
                                                             ds:_2015_total ?total_pop_2015 ;\
                                                             ds:_2015_male_total ?total_male_2015 ;\
                                                             ds:_2015_female_total ?total_female_2015 .\
                                                         ?ns3 ds:name ?name ;\
                                                              ds:address ?address ;\
                                                              ds:listyear ?list_year ;\
                                                              ds:assessedvalue ?assessed_value ;\
                                                              ds:saleprice ?sales_price ;\
                                                              ds:salesratio ?sales_ratio ;\
                                                               ds:location  ?ns4 .\
                                                         ?ns4  geo:lat ?lat ;\
                                              geo:long ?long.\
                                          FILTER(?name=\""+ string + "\")\
                                        }"
                                     },
                                     function(data, status){

/*var p1 = new google.maps.LatLng(45.463688, 9.18814);
var p2 = new google.maps.LatLng(46.0438317, 9.75936230000002);

alert(calcDistance(p1, p2));

//calculates distance between two points in km's
function calcDistance(p1, p2) {
  return (google.maps.geometry.spherical.computeDistanceBetween(p1, p2) / 1000).toFixed(2);
}*/


                                       angular.forEach($scope.markers, function(marker){
                                          marker.setMap(null);
                                        });
                                for(var i in data.results.bindings)
                                {
                                     		createMarker({
                                                       city : data.results.bindings[i].address.value,
                                                       desc : "Price: $" + data.results.bindings[i].sales_price.value,
                                                       lat : data.results.bindings[i].lat.value,
                                                       long : data.results.bindings[i].long.value
                                                   });
                                                   $scope.map.fitBounds(bounds);
                                }
                                     });
                          }

              var mapOptions = {
                  zoom: 9,
                  center: new google.maps.LatLng(33.3920461,-111.9981417),
                  mapTypeId: google.maps.MapTypeId.TERRAIN
              }

              $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);

              $scope.markers = [];

              var infoWindow = new google.maps.InfoWindow();

              var createMarker = function (info){

                  var marker = new google.maps.Marker({
                      map: $scope.map,
                      position: new google.maps.LatLng(info.lat, info.long),
                      title: info.city
                  });
                  marker.content = '<div class="infoWindowContent">' + info.desc + '</div>';

                  google.maps.event.addListener(marker, 'click', function(){
                      infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
                      infoWindow.open($scope.map, marker);
                  });

                  $scope.markers.push(marker);
                  bounds.extend(marker.position);

              }

          });
