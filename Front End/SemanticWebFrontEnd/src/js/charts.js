function drawHousingChart(housingData) {
    console.log('inside drawHousingChart');
    Highcharts.chart('housingContainer', {
      chart: {
          type: 'column',
          backgroundColor:'rgba(255, 255, 255, 0.1)'
      },
      title: {
          text: 'Housing stats'
      },
      subtitle: {
          text: 'Source: ct.data'
      },
      xAxis: {
          type: 'category'
      },
      yAxis: {
          title: {
              text: '# Houses'
          }

      },
      legend: {
          enabled: false
      },
      plotOptions: {
          series: {
              borderWidth: 0,
              dataLabels: {
                  enabled: true

              }
          }
      },

      tooltip: {
          headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
          pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
      },

      series: [{
          name: 'Brands',
          colorByPoint: true,
          data: [{
              name: 'Total',
              y: housingData.totalHouses
          }, {
              name: 'Occupied',
              y: housingData.occupiedHouses
          }, {
              name: 'Vacant',
              y: housingData.vacantHouses
          }]
      }]
    })
}


function drawCrimeChart(crimeData){
      console.log('inside drawCrimeChart');
      Highcharts.chart('arrestContainer', {

        chart: {
            type: 'gauge',
            alignTicks: false,
            plotBackgroundColor: null,
            backgroundColor:'rgba(79, 206, 238, 0.1)',
            plotBackgroundImage: null,
            plotBorderWidth: 0,
            plotShadow: false
        },

        title: {
            text: 'Arrest rate'
        },

        pane: {
            startAngle: -150,
            endAngle: 150
        },

        yAxis: [{
            min: 0,
            max: 2225,
            lineColor: '#339',
            tickColor: '#339',
            minorTickColor: '#339',
            offset: -25,
            lineWidth: 2,
            labels: {
                distance: -20,
                rotation: 'auto'
            },
            tickLength: 5,
            minorTickLength: 5,
            endOnTick: false
        }, {

            tickPosition: 'outside',
            lineColor: '#933',
            lineWidth: 2,
            minorTickPosition: 'outside',
            tickColor: '#933',
            minorTickColor: '#933',
            tickLength: 5,
            minorTickLength: 5,
            labels: {
                distance: 12,
                rotation: 'auto'
            },
            offset: -20,
            endOnTick: false
        }],

        series: [{
            name: 'Arrest rate',
            data: [parseFloat(crimeData.arrestRate)],
            dataLabels: {
                formatter: function () {
                    var kmh = this.y;
                    return '<span style="color:#339">' + kmh + ' per 100,000</span><br/>';
                },
                backgroundColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, '#DDD'],
                        [1, '#FFF']
                    ]
                }
            },
            tooltip: {
                valueSuffix: ' km/h'
            }
        }]

    },
        function (chart) {
          if (chart.axes) {
              var point = chart.series[0].points[0]
              point.update(chart.series[0].points[0]);
          }
    });


    Highcharts.chart('crimeContainer', {

      chart: {
          type: 'gauge',
          alignTicks: false,
          plotBackgroundColor: null,
          plotBackgroundImage: null,
          backgroundColor:'rgba(79, 206, 238, 0.1)',
          plotBorderWidth: 0,
          plotShadow: false
      },

      title: {
          text: 'Crime rate'
      },

      pane: {
          startAngle: -150,
          endAngle: 150
      },

      yAxis: [{
          min: 0,
          max: 5000,
          lineColor: '#339',
          tickColor: '#339',
          minorTickColor: '#339',
          offset: -25,
          lineWidth: 2,
          labels: {
              distance: -20,
              rotation: 'auto'
          },
          tickLength: 5,
          minorTickLength: 5,
          endOnTick: false
      }, {

          tickPosition: 'outside',
          lineColor: '#933',
          lineWidth: 2,
          minorTickPosition: 'outside',
          tickColor: '#933',
          minorTickColor: '#933',
          tickLength: 5,
          minorTickLength: 5,
          labels: {
              distance: 12,
              rotation: 'auto'
          },
          offset: -20,
          endOnTick: false
      }],

      series: [{
          name: 'Arrest rate',
          data: [parseFloat(crimeData.crimeRate)],
          dataLabels: {
              formatter: function () {
                  var kmh = this.y;
                  return '<span style="color:#339">' + kmh + ' per 100,000</span><br/>';
              },
              backgroundColor: {
                  linearGradient: {
                      x1: 0,
                      y1: 0,
                      x2: 0,
                      y2: 1
                  },
                  stops: [
                      [0, '#DDD'],
                      [1, '#FFF']
                  ]
              }
          },
          tooltip: {
              valueSuffix: ' km/h'
          }
      }]

  },
      function (chart) {
        if (chart.axes) {
            var point = chart.series[0].points[0];
            point.update(chart.series[0].points[0]);
        }
  });
}
