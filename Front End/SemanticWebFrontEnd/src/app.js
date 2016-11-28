(function(){
  angular.module("myapp",[])
    .controller("usercontroller", ['$scope', '$filter', '$http', usercontroller]);

    function usercontroller($scope, $http, $filter){
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
             if(string.length != 0){
               angular.forEach($scope.townList, function(town){
                    if(town.toLowerCase().startsWith(string.toLowerCase()))
                    {
                         output.push(town);
                    }
               });
               $scope.filterTown = output;
             }else{
               $scope.hidethis = true;
             }
        }

        $scope.fillTextbox = function(string){
              // console.log("fillTextbox called");
             $scope.hidethis = true;
             $scope.town = string;

        }

        // $http.get('townData.json').success(function(data){
        //     $scope.jdata = data;
        // });

        // $scope.populateFields = function(str){
        //     var cityData = data(str);
        //     $scope.crime_rate = cityData('crime_rate');
        //     $scope.average_price = cityData('average_price');
        //     $scope.school = cityData('school');
        //     $scope.medical = cityData('medical');
        // }
   }

})();
