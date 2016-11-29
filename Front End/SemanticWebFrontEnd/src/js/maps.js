//Data

//Angular App Module and Controller
var sampleApp = angular.module('mapsApp', []);
sampleApp.controller('MapCtrl', function($scope) {
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

  $scope.complete = function(string) {
    $scope.hidethis = false;
    var output = [];
    angular.forEach($scope.townList, function(town) {
      if (town.toLowerCase().indexOf(string.toLowerCase()) >= 0) {
        output.push(town);
      }
    });
    $scope.filterTown = output;
  }

  $scope.fillTextbox = function(string) {
    $scope.hidethis = true;
    $scope.town = string;

    $.post("http://localhost:3030/ds/query", {
        query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/>\
                                        PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#>\
                                        select DISTINCT ?name \
                                                         ?address ?sales_price ?lat ?long where {\
                                                         ?ns1 ds:municipality ?name .\
                                                         ?ns3 ds:name ?name ;\
                                                              ds:address ?address ;\
                                                              ds:saleprice ?sales_price ;\
                                                               ds:location  ?ns4 .\
                                                         ?ns4  geo:lat ?lat ;\
                                              geo:long ?long.\
                                          FILTER(?name=\"" + string + "\")\
                                        }"
      },
      function(data, status) {
        angular.forEach($scope.markers, function(marker) {
          marker.setMap(null);
        });
        for (var i in data.results.bindings) {
          createMarker({
            city: data.results.bindings[i].address.value,
            desc: "Price: $" + data.results.bindings[i].sales_price.value,
            lat: data.results.bindings[i].lat.value,
            long: data.results.bindings[i].long.value
          });
          $scope.map.fitBounds(bounds);
        }
      });
  }

  var mapOptions = {
    zoom: 9,
    center: new google.maps.LatLng(33.3920461, -111.9981417),
    mapTypeId: google.maps.MapTypeId.TERRAIN
  }

  $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);

  $scope.markers = [];

  var infoWindow = new google.maps.InfoWindow();

  var createMarker = function(info) {

    var marker = new google.maps.Marker({
      map: $scope.map,
      position: new google.maps.LatLng(info.lat, info.long),
      title: info.city
    });
    marker.content = '<div class="infoWindowContent">' + info.desc + '</div>';

    google.maps.event.addListener(marker, 'click', function() {
      infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
      infoWindow.open($scope.map, marker);
    });

    $scope.markers.push(marker);
    bounds.extend(marker.position);
  }

});
