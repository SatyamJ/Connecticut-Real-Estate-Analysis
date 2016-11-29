(function(){
  angular.module("myapp",[])
    .controller("usercontroller", ['$scope', '$filter', '$http', usercontroller]);


    function usercontroller($scope, $filter, $http){
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
        $scope.hideTownData = true;
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
               $scope.hideTownData = true;
             }
        }

        $scope.fillTextbox = function(string){
              // console.log("fillTextbox called");
             $scope.hidethis = true;
             $scope.town = string;
             $scope.populateFields(string)

        }

        $scope.populateFields = function(town_name){
            $scope.hideTownData = false;
            $scope.selectedTown = town_name;
            $scope.populateCrimeData(town_name);
            $scope.populateHousingData(town_name);
            $scope.populatePoliceDeptData(town_name);
            $scope.populateSchoolData(town_name);
            $scope.populateDemographicData(town_name);
            $scope.populateRealEstateData(town_name)
          };

          $scope.populateCrimeData = function(town_name){

              $.post("http://localhost:3030/ds/query",
              {

                query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/>\
                        PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#>\
                        SELECT ?crime_reported ?population ?arrests \
                        WHERE{ \
                          ?cro ds:jurisdiction \""+town_name+"\". \
                          ?cro ds:crime_reported ?crime_reported . \
                          ?cro ds:population ?population . \
                          ?cra ds:"+town_name.toLowerCase()+" ?arrests \
                        }"

              }, function(data, status){
                    // console.log(data);
                    var crime_reported  = data.results.bindings[0].crime_reported.value;
                    var population = data.results.bindings[0].population.value;
                    var arrests = data.results.bindings[0].arrests.value;
                    console.log('Got crime data');
                    $scope.crime_rate = (crime_reported/population) * 100000;
                    $scope.arrest_rate = (arrests/population) * 100000;
                }
              );
            };

            $scope.populateHousingData = function(town_name){

                $.post("http://ec2-54-149-233-81.us-west-2.compute.amazonaws.com:3030/ds/query",
                {

                  query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \
                          PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#> \
                          select ?total_houses ?occupied_houses ?vacant_houses \
                          where { \
                          ?ns1 ds:municipality \""+town_name+"\"; \
                            ds:total_housing_units ?total_houses ; \
                            ds:occupied_housing_units ?occupied_houses ; \
                            ds:vacant_housing_units ?vacant_houses . \
                          }"

                }, function(data, status){
                      console.log('got housing data');

                      $scope.total_houses  = data.results.bindings[0].total_houses.value;
                      // console.log('total_houses: '+$scope.total_houses);
                      $scope.occupied_houses = data.results.bindings[0].occupied_houses.value;
                      // console.log('occupied_houses: '+$scope.occupied_houses);
                      $scope.vacant_houses = data.results.bindings[0].vacant_houses.value;
                      // console.log('vacant_houses:'+$scope.vacant_houses);

                  }
                );
              };

              $scope.populatePoliceDeptData = function(town_name){

                  $.post("http://localhost:3030/ds/query",
                  {

                    query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \
                            SELECT  (count(?pd) as ?pd_count) \
                            WHERE{ \
                              ?pd ds:city \""+town_name.toUpperCase()+"\" \
                            }"

                  }, function(data, status){
                        console.log('got Police Dept  data');

                        $scope.policeDeptCount  = data.results.bindings[0].pd_count.value;
                        // console.log('policeDeptCount: '+$scope.policeDeptCount);


                    }
                  );
              };

              $scope.populateSchoolData = function(town_name){

                    $.post("http://localhost:3030/ds/query",
                    {

                      query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \
                              SELECT (count(?s) as ?schools_count) \
                              WHERE{ \
                                ?s ds:district_name \""+town_name+"\" \
                              } "

                    }, function(data, status){
                          console.log('got school data');

                          $scope.schools_count  = data.results.bindings[0].schools_count.value;
                          // console.log('schools_count: '+$scope.schools_count);


                      }
                    );
              };

              $scope.populateDemographicData = function(town_name){

                    $.post("http://localhost:3030/ds/query",
                    {

                      query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \
                              select \
                              ?total_pop_2015 ?total_male_2015 ?total_female_2015 \
                              where { \
	                               ?ns2 ds:town \""+town_name+"\" ; \
	                                ds:_2015_total ?total_pop_2015 ; \
                                  ds:_2015_male_total ?total_male_2015 ; \
                                  ds:_2015_female_total ?total_female_2015 .	\
                                }"

                    }, function(data, status){
                          console.log('got demographic data');

                          $scope.population  = data.results.bindings[0].total_pop_2015.value;
                          // console.log('population: '+$scope.population);
                          var males = parseInt(data.results.bindings[0].total_male_2015.value);
                          var females = parseInt(data.results.bindings[0].total_female_2015.value);
                          $scope.sex_ratio = (males/females) * 100;
                          // console.log('sex_ratio: '+$scope.sex_ratio);
                      }
                    );
              };

              $scope.populateRealEstateData = function(town_name){

                    $.post("http://localhost:3030/ds/query",
                    {

                      query: "PREFIX ds:<http://data.ct.gov/resource/igy9-udjm/> \
                              select ?assessed_value ?sales_price ?sales_ratio \
                              where \
                                {  \
	                                  ?ns ds:name \""+town_name+"\"; \
  		                              ds:assessedvalue ?assessed_value ; \
  		                              ds:saleprice ?sales_price ; \
 		                                ds:salesratio ?sales_ratio . \
                                } "

                    }, function(data, status){
                          console.log('got real estate data');
                          var assessed_value_sum = 0;
                          var sales_price_sum = 0;
                          var sales_ratio_sum = 0;


                          if(data.results.bindings.length!=0){
                            for(var res in data.results.bindings){
                                assessed_value_sum += parseFloat(data.results.bindings[res].assessed_value.value);
                                sales_price_sum += parseFloat(data.results.bindings[res].sales_price.value);
                                sales_ratio_sum += parseFloat(data.results.bindings[res].sales_ratio.value);
                            }

                            $scope.avgAssessedValue = assessed_value_sum/data.results.bindings.length;
                            // console.log('avgAssessedValue: '+$scope.avgAssessedValue);
                            $scope.avgSalesPrice = sales_price_sum/data.results.bindings.length;
                            // console.log('avgSalesPrice: '+$scope.avgSalesPrice);
                            $scope.avgSalesRatio = sales_ratio_sum/data.results.bindings.length;
                            // console.log('avgSalesRatio: '+$scope.avgSalesRatio);
                          }else{
                            $scope.avgAssessedValue = "Record not found!";
                            // console.log('avgAssessedValue: '+$scope.avgAssessedValue);
                            $scope.avgSalesPrice = "Record not found!";
                            // console.log('avgSalesPrice: '+$scope.avgSalesPrice);
                            $scope.avgSalesRatio = "Record not found!";
                            // console.log('avgSalesRatio: '+$scope.avgSalesRatio);
                          }
                      }
                    );
              };
        }
})();
