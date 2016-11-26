(function(){
  var app = angular.module("myapp",[]);
   app.controller("usercontroller", function($scope){
        $scope.townList = [
          "andover", "ansonia", "ashford", "avon", "barkhamsted", "beacon falls",
          "berlin", "bethany", "bethel", "bethlehem", "bloomfield", "bolton", "bozrah",
          "branford", "bridgeport", "bridgewater", "bristol", "brookfield", "brooklyn",
          "burlington", "canaan", "canterbury", "canton", "chaplin", "cheshire",
          "chester", "clinton", "colchester", "colebrook", "columbia", "cornwall",
          "coventry", "cromwell", "danbury", "darien", "deep river", "derby", "durham",
          "eastford", "east granby", "east haddam", "east hampton", "east hartford",
          "east_haven", "east_lyme", "easton", "east_windsor", "ellington", "enfield",
          "essex", "fairfield", "farmington", "franklin", "glastonbury", "goshen",
          "granby", "greenwich", "griswold", "groton city", "guilford", "haddam",
          "hamden", "hampton", "hartford", "hartland", "harwinton", "hebron",
          "kent", "killingly", "killingworth", "lebanon", "ledyard", "lisbon",
          "litchfield", "lyme", "madison", "manchester", "mansfield", "marlborough",
          "meriden", "middlebury", "middlefield", "middletown", "milford",
          "monroe", "montville", "morris", "naugatuck", "new britain", "new canaan",
          "new fairfield", "new hartford", "new_haven", "newington", "new_london",
          "new milford", "newtown", "norfolk", "north branford", "north canaan",
          "north haven", "north stonington", "norwalk", "norwich", "old lyme",
          "old saybrook", "orange", "oxford", "plainfield", "plainville", "plymouth",
          "pomfret", "portland", "preston", "prospect", "putnam municipal",
          "putnam", "redding", "ridgefield", "rocky hill", "roxbury", "salem",
          "salisbury", "scotland", "seymour", "sharon", "shelton", "sherman",
          "simsbury", "somers", "southbury", "southington", "south_windsor", "sprague",
          "stafford", "stamford", "sterling", "stonington", "stratford", "suffield",
          "thomaston", "thompson", "tolland", "torrington", "trumbull", "union",
          "vernon", "voluntown", "wallingford", "warren", "washington", "waterbury",
          "waterford", "watertown", "westbrook", "west_hartford", "west_haven", "weston",
          "westport", "wethersfield", "willington", "wilton", "winchester", "willimantic",
          "windham", "windsor", "windsor locks", "wolcott", "woodbridge", "woodbury",
          "woodstock", "yale university", "uconn storrs", "foxwoods casino", "mohegan sun casino",
          "mta", "c c s u", "e c s u", "uconn health ctr", "w c s u", "s c s u", "state capitol",
          "groton town", "groton long pt", "state police misc", "mashantucket tribal pd",
          "mohegan tribal pd", "fairfield county", "hartford county", "litchfield_county",
          "middlesex county", "new haven county", "new london county", "tolland county",
          "windham county", "new york"
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
             $scope.town = string;
             $scope.hidethis = true;
        }
   });

})();
