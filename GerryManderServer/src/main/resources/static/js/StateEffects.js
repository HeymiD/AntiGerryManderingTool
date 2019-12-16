function dicSize(x){
    var  len = 0;
    for (var item in x){
        len ++;
    }
    return len;
}

function recenterMap(){ map.setView(usCenter,5); }

function fetchDistrict(e){
        $.ajax({
            url:"http://localhost:8080/state",
            data:{
                state: e.feature.properties.name
            },
            success: function(response){
                    precinctKeys = JSON.parse(response)
                    // console.log(precinctKeys)
                    stateInit = 1;
                    if($('#districtContent').hasClass('active')){
                			districtData.show();
                		}
                		else{
                			precinctData.show();
                		}
                    $('#precinctContent').prop('disabled', false);
                    electionDic['Presidential2016'] = getPrecinctData(electionSetting);
                    stateData.update();
                    stateData.show();
            },
            error:function(err){
                console.log(err, "ERROR");
            }
        })

//    }
 }

function getDistrict(e,districtId){
	$.ajax({
		url:"http://localhost:8080/state",
		data: {
			state: e.feature.properties.name,
			districtId: districtId
		},
		success: function(response){
			// console.log(response);
			var district = JSON.parse(response);
			var districtLayer = L.geoJson(district, {
			    			style: districtStyle,
			    			onEachFeature: districtEffects
			    		});
			    districtLayer.addTo(distLayer);
			    districtx[districtId] = districtLayer;
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}


function getPrecincts(e,districtId){
	$.ajax({
		url:"http://localhost:8080/precincts",
		data: {
			state: e.feature.properties.name,
			districtId: districtId,
		},
		success: function(response){
//  			 console.log(response);
             var precinct = JSON.parse(response);
             var precinctLayer = L.geoJson(precinct, {
                             style: precinctStyle,
                             onEachFeature: precinctEffects
                         });
                map.addLayer(precinctLayer);
                  precinctLayer.addTo(precLayer);
//                  precinctDict[districtId]=precinctLayer;
                  distPrecinct[districtId] = precinctLayer;
  //            console.log('distID', districtId, 'curr stuff = ', distPrecinct);

		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		},

	})
}

function getPrecinctsByDistrictInit(e,districtId){
	$.ajax({
		url:"http://localhost:8080/precinctsInit",
		data: {
			state: e.feature.properties.name,
		},
		success: function(response){
  			 getPrecinctsByDistrict(e,districtId)
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		},
	})
}

function getPrecinctsByDistrict(e,districtId){
    var increment = 0;
    var done = 1

    while(increment>-1){
        if(increment==0){
                districtId = "Begin "+districtId
            }
        $.ajax({
        		url:"http://localhost:8080/precincts",
        		data: {
        			state: e.feature.properties.name,
        			districtId: districtId
        		},
        		success: function(response){
          			 // console.log(response);
                    if(response==="District Done"){
                        done=1
                        return
                    }
                     var precinct = JSON.parse(response);
                     var precinctLayer = L.geoJson(precinct, {
                                     style: precinctStyle,
                                     onEachFeature: precinctEffects
                                 });
                        map.addLayer(precinctLayer);
                        precLayer.addLayer(precinctLayer)
                        return
        		},
        		error:function(err){
        		console.log(err)
        		console.log("ERROR")
        		},
        		async: false
        	})
        	increment=increment+1
        	if(done==1){break}
    }

}



function getDistrictData(districtId,elecType){
 $.ajax({
 		url:"http://localhost:8080/districtData",
 		data: {
 			districtId: districtId,
 			elecType: elecType
 		},
 		success: function(response){
 		// console.log(response)
 		var data = JSON.parse(response)
 		districtxData[data.DistrictID]=data
 		districtData.update(data)

 		},
 		error:function(err){
 		console.log(err)
 		console.log("ERROR")
 		}
 	})
 }

function getPrecinctData(elecType){
$.ajax({
		url:"http://localhost:8080/precincts",
		data: {
            electionType : elecType
		},
		success: function(response){
		// console.log(response)
		var data = JSON.parse(response)
        electionDic[elecType] = data
//		precinctData.update(data)

		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

phase0btn.on('click', function(){
    // console.log(votingThresh + "     " + blocThresh);
  getPhaseZeroData(electionSetting, votingThresh/100, blocThresh/100 );
})
function getPhaseZeroData(elecType,votingThreshold,blockThreshold){
$.ajax({
		url:"http://localhost:8080/phase0",
		data: {
			votingThreshold: votingThreshold,
			blockThreshold: blockThreshold,
			electionType: elecType
		},
		success: function(response){
		// console.log(response)
		phase0Data = JSON.parse(response)
        updatePhase0(phase0Data);

		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

function getPhaseOneData(elecType,votingThreshold,blockThreshold, targetNumDistricts, demString, update,begin){
    var result;
    $.ajax({
		url:"http://localhost:8080/phase1",
		async:  !update,
		data: {
			electionType: elecType,
			votingThreshold: votingThreshold/100,
			blockThreshold: blockThreshold/100,
			update: true,
			targetNumDistricts : targetNumDistricts,
			demString : demString,
			begin: begin,
      fairnessMin : fairnessMin/100,
      reokCompactMin : reokCompactMin/100,
      convexCompactMin : convexCompactMin/100,
      edgeCompactMin : edgeCompactMin/100,
      popEqualMin : popEqualMin/100,
      popHomoMin : popHomoMin/100,
      efficiencyMin : efficiencyMin/100,
      competitiveMin : competitiveMin/100,
      republicGerryMin : republicGerryMin/100,
      democratGerryMin : democratGerryMin/100,
      countyCompactMin : countyCompactMin/100
		},
		success: function(response){
//            alert(response === "done")
            if(response === "done"){
                alert("Phase 1 Completed")
                phase2DistrictTable()
                return 1
            }
            else{
                result = JSON.parse(response);
                        precLayer.eachLayer(function(layer){
                              layer.eachLayer(function(layer2){
                                                   var precKey = layer2.feature.properties.PCTKEY;
            //                                       var districtId = phase1Data[precKey];
                                                    var districtId = result[precKey];
                                       //            console.log('precKey '+precKey+' currPrec '+ currPrec);
            //                                       console.log(layer2);

                                                   layer2.setStyle({
                                                       weight: 2,
                                                       opacity: 1,
                                                       color: 'white',
                                                       dashArray: '3',
                                                       fillOpacity: 0.7,
                                                       fillColor: precNewDemoColor(districtId)
                                                   });
                                               });
                                         });
                algoBegin=false
                if(update == false){
                    getPhaseOneData(elecType,votingThreshold,blockThreshold, targetNumDistricts, demString,update,false);
                }
            }
//            phase1Data = JSON.parse(response)

//            if(update == false){
//                getPhaseOneData(elecType,votingThreshold,blockThreshold, targetNumDistricts, demString,update);
//
//            }
//            return phase1Data;
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}

	});

}
function getPhaseTwoData(elecType,votingThreshold,blockThreshold, targetNumDistricts, demString, begin){
var result;
    $.ajax({
		url:"http://localhost:8080/phase2",
		async:  true,
		data: {
			electionType: elecType,
			votingThreshold: votingThreshold/100,
			blockThreshold: blockThreshold/100,
			targetNumDistricts : targetNumDistricts,
			demString : demString,
			begin : begin,
      fairnessMin : fairnessMin/100,
      reokCompactMin : reokCompactMin/100,
      convexCompactMin : convexCompactMin/100,
      edgeCompactMin : edgeCompactMin/100,
      popEqualMin : popEqualMin/100,
      popHomoMin : popHomoMin/100,
      efficiencyMin : efficiencyMin/100,
      competitiveMin : competitiveMin/100,
      republicGerryMin : republicGerryMin/100,
      democratGerryMin : democratGerryMin/100,
      countyCompactMin : countyCompactMin/100
		},
		success: function(response){
//            alert(response === "done")
            if(response === "done"){
                alert("Phase 2 Completed")
                phase2res();
                return 1
            }
            if(response!=""){
            result = JSON.parse(response);
            precLayer.eachLayer(function(layer){
                  layer.eachLayer(function(layer2){
                                       var precKey = layer2.feature.properties.PCTKEY;
                                       if(precKey in result){
                                            var districtId = result[precKey];
                                            // console.log('precKey '+precKey+' districtId '+ districtId);
//                                            console.log(layer2);
                                            layer2.setStyle({
                                                           weight: 2,
                                                           opacity: 1,
                                                           color: 'white',
                                                           dashArray: '3',
                                                           fillOpacity: 0.7,
                                                           fillColor: precNewDemoColor(districtId)
                                                       });

                                       }
                                   });
                             });
            }
//            phase1Data = JSON.parse(response)

            getPhaseTwoData(elecType,votingThreshold,blockThreshold, targetNumDistricts, demString, false);

		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}

	});

}
function phase2res(){
  $.ajax({
    url:"http://localhost:8080/phase2Scores",
    success: function(response){
      p2res = JSON.parse(response);
      console.log(p2res);
      update2Res(p2res);
      phase2DistrictTable();
      return
    },
    error:function(err){
    console.log(err)
    console.log("ERROR")
    }

});
}

function update2Res(props){
  // var totVotes = props.Republican + props.Democrat + props.Green + props.Libertarian;
	var results2 = $('#p2results');
	results2.html(
	'<h3>Phase 2 Results </h3>'
	+ '<br/><b>Old Gerrymandering Scores: </b>' + parseFloat(props.OldGmScore).toFixed(2)
  + '%<br/><b>New Gerrymandering Scores: </b>' + parseFloat(props.NewGmScore).toFixed(2) + '%'
  // + '%<br/><h4>Voting Distribution</h4>'
  // + '<br/><b>Republican Districts: </b>' + parseFloat(props.Republican).toFixed(2)
  // + '<br/><b>Democrat Districts: </b>' + parseFloat(props.Democrat).toFixed(2)
  // + '<br/><b>Green Districts: </b>' + parseFloat(props.Green).toFixed(2)
  // + '<br/><b>Libertarian Districts: </b>' + parseFloat(props.Libertarian).toFixed(2)
);
}

function phase2DistrictTable(){
  $.ajax({
    url:"http://localhost:8080/FinalResult",
    success: function(response){
      p2disttable = response;
      // console.log(p2disttable);
      update2DistTable(p2disttable);
      return
    },
    error:function(err){
    console.log(err)
    console.log("ERROR")
    }

});
}

function update2DistTable(props){
  // var totVotes = props.Republican + props.Democrat + props.Green + props.Libertarian;
	var results2 = $('#p2distable');
  props = props.replace('\n','<br/>')
	results2.html(props);
}

//==================== State Zone ====================
function stateColor(d){
	return d > 3 ? '#ffb0a0' : d > 12  ? '#a0d4ff' : '#d7ffa0';
}

function stateStyle(feature){ //need to change the way we set colors based on ???
  // console.log(feature.properties.COLOR);
	return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
        fillColor: stateColor(feature.properties.population)
	};
}

function onStateHover(e) {
    var layer = e.target;

    layer.setStyle({
        weight: 5,
        color: '#ffe135',
        dashArray: '',
        fillOpacity: 0.7
    });

    if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
        layer.bringToFront();
    }
//    stateData.update(layer.feature.properties);
    // if(stateInit != 1){
    //   fetchDistrict(e.target);
    // }

}

function stateMouseOut(e) {
  var layer = e.target;
  layer.setStyle({
    weight: 2,
    color: 'white',
    dashArray: '3',
    fillOpacity: 0.7,
  });
}

function zoomOnState(e) {

    currState = e.target.feature.properties.name;
     // console.log(currState);
     if(stateInit != 1){
       fetchDistrict(e.target);
     }
     else{
       districtData.show();
       stateData.show();
       stateData.update();
     }
    stateMouseOut(e);

    map.removeLayer(texas)
    map.addLayer(texasDistrictsLayer)
//    map.removeLayer(geojson);
//    map.addLayer(distLayer);
//    map.addLayer(precLayer);
	map.fitBounds(e.target.getBounds());
//	map.on('drag', function() {
//	  map.panInsideBounds(e.target.getBounds(), { animate: false });
//	});
	select.select.selectedIndex = 1;
	mapContent.show();
//    console.log(distPrecinct);
	// console.log(!outerMenuBtn.is(":visible
	// outerMenuBtn.show();
	if(body.attr('class') == 'active-nav'){ outerMenuBtn.hide(); }
	else{ outerMenuBtn.show(); }
	stateData.hide();

  if(stateInit){ districtData.show(); }
  $(".leaflet-control-zoom").css("display","block");
}
// zoomin = $(".leaflet-control-zoom");
// console.log(zoomin);

function stateEffects(feature, layer) {
	layer.on({
		mouseover: onStateHover,
		mouseout: stateMouseOut,
		click: zoomOnState
	});
}

//==================== District Zone ====================
function districtColor(d){ return disColor[d] }

function districtStyle(feature){ //need to change the way we set colors based on ???
	return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
        fillColor: districtColor(feature.properties.fid)
	};
}

function onDistrictHover(e) {
//    distLayer.getLayers().length==36
var layer = e.target;
// console.log(e);
layer.setStyle({
    weight: 5,
    color: '#ffe135',
    dashArray: '',
    fillOpacity: 0.7
});
if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
layer.bringToFront();
}
    if(stateInit==1){
        // console.log("Getting district data")
        var districtId = "U.S. Rep "+layer.feature.properties.fid
        if(!(districtId in districtxData)){
    //        console.log('electionType.val()+electionYear.val() = '+ electionType.val() + electionYear.val())
              // console.log('in districtID' + districtId);
            getDistrictData(districtId, electionSetting);}
        else{
          // console.log(incumbs[layer.feature.properties.fid]);
            districtData.update(districtxData[districtId],layer.feature.properties.fid)
        }
        //    console.log(distData.DistrictID+", "+distData.Population)
        //    districtData.update(distData)
    }
}

function districtMouseOut(e) {
//	geojson.resetStyle(e.target);
//    geoJson.style = districtStyle;
//	distLayer.resetStyle(e.target);
    var layer = e.target;
    layer.setStyle({
        weight: 2,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
    });
	districtData.update();
}

function zoomOnDistrict(e) { map.fitBounds(e.target.getBounds()); }

function districtEffects(feature, layer) {
	layer.on({
		mouseover: onDistrictHover,
		mouseout: districtMouseOut,
		// click: zoomOnDistrict
	});
}

//==================== Precinct Zone ====================
function precinctColor(r, d, g, l){
    var power = Math.max(r,d,g,l)
	return r == power ? '#E9141D' : d == power ? '#0015BC' : g == power  ? '#00ff00 ' : '#FED105';
}


function precinctDemoColor(demographic, currPrec){
    var demval;
//    console.log(demographic)
    switch(demographic){
        case 'White':
          demval = currPrec.White / currPrec.Population;
          return demval > 0.75  ? '#9f1499' :
                 demval > 0.5  ? '#cc1ac4' :
                 demval > 0.4  ? '#e533de' :
                 demval > 0.3   ? '#f18eed' :
                 demval > 0.2   ? '#f18eed' :
                 demval > 0.1   ? '#f6bbf4' :
                                  '#fce8fb';
          break;
        case 'Black':
          demval = currPrec.Black / currPrec.Population;
          return demval > 0.75  ? '#0a35a9' :
                 demval > 0.5  ? '#0c44d9' :
                 demval > 0.4  ? '#265ef3' :
                 demval > 0.3   ? '#5682f5' :
                 demval > 0.2   ? '#86a5f8' :
                 demval > 0.1   ? '#b7c9fb' :
                                  '#e7edfe';
          break;
        case 'Hispanic':
          demval = currPrec.Hispanic / currPrec.Population;
          return demval > 0.75  ? '#36a310' :
                 demval > 0.5  ? '#45d114' :
                 demval > 0.4  ? '#5feb2e' :
                 demval > 0.3   ? '#82ef5c' :
                 demval > 0.2   ? '#a6f48b' :
                 demval > 0.1   ? '#edfde8' :
                                  '#edfde8';
          break;
        case 'Native':
          demval = currPrec.Native / currPrec.Population;
          return demval > 0.75  ? '#99b102' :
                 demval > 0.5  ? '#c4e302' :
                 demval > 0.4  ? '#defd1c' :
                 demval > 0.3   ? '#e5fd4e' :
                 demval > 0.2   ? '#edfe81' :
                 demval > 0.1   ? '#f4feb3' :
                                  '#fbffe6';

          break;
        case 'Pacific':
          demval = currPrec.Pacific / currPrec.Population;
          return demval > 0.75  ? '#17899c' :
                 demval > 0.5  ? '#1db0c8' :
                 demval > 0.4  ? '#37c9e2' :
                 demval > 0.3   ? '#63d5e8' :
                 demval > 0.2   ? '#90e1ef' :
                 demval > 0.1   ? '#bcedf5' :
                                  '#e9f9fc';

          break;
        case 'Asian':
          demval = currPrec.Asian / currPrec.Population;
          return demval > 0.75  ? '#aa5709' :
                 demval > 0.5  ? '#da700b' :
                 demval > 0.4  ? '#f48925' :
                 demval > 0.3   ? '#f6a455' :
                 demval > 0.2   ? '#f9be86' :
                 demval > 0.1   ? '#fbd8b6' :
                                  '#fbd8b6';

          break;
        case 'Other':
          demval = currPrec.Other / currPrec.Population;
          return demval > 0.75  ? '#9c1733' :
                 demval > 0.5  ? '#c81d41' :
                 demval > 0.4  ? '#e2375b' :
                 demval > 0.3   ? '#e8637f' :
                 demval > 0.2   ? '#ef90a4' :
                 demval > 0.1   ? '#f5bcc8' :
                                  '#fce9ed';

          break;

    }
}

targetDis.on('change',function(){
    targetDisVal = targetDis.val();
//    alert(targetDisVal);
    $('#runAlgo').prop('disabled', false);

});
function precNewDemoColor(newDisId){
  // precinct key , district ID   -> color precincts based on
//    console.log(demographic)
    newDisId = parseInt(newDisId.slice(newDisId.lastIndexOf(" ")));
//    alert(newDisId);
    return selectColor(newDisId, 10);
}

function selectColor(colorNum, colors){
    if (colors < 1) colors = 1; // defaults to one color - avoid divide by zero
      return "hsl(" + (colorNum * (360 / colors) % 360) + ",100%,50%)";
}
// var color = selectColor(8, 13);
function precinctStyle(feature){
    var precKey = feature.properties.PCTKEY;
    var currPrec = precinctKeys[precKey];

    return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
        fillColor: precinctDemoColor(selectedDemo, currPrec)
        // fillColor: precinctColor(feature.properties.Republican, feature.properties.Democrat, feature.properties.Green, feature.properties.Libertarian)
    };
}

function onPrecinctHover(e) {
	var layer = e.target;
	// console.log(e);
	layer.setStyle({
	weight: 5,
	color: '#ffe135',
	dashArray: '',
	fillOpacity: 0.7
  });
  if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
  	layer.bringToFront();
  }
  var precKey = layer.feature.properties.PCTKEY;
  var currPrec = precinctKeys[precKey];
  var elecPrec = electionDic[electionSetting][precKey]
  precinctData.update(currPrec, elecPrec, precKey);
}

function precinctMouseOut(e) {
    var layer = e.target;
    layer.setStyle({
        weight: 2,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
    });
	precinctData.update();
}

function zoomOnPrecinct(e) { map.fitBounds(e.target.getBounds()); }

function precinctEffects(feature, layer) {
	layer.on({
		mouseover: onPrecinctHover,
		mouseout: precinctMouseOut,
		// click: zoomOnPrecinct
	});
}


demoOpts.on('change',function(){
  selectedDemo = $("input[name='demoType']:checked").val();
  precLayer.eachLayer(function(layer){
        layer.eachLayer(function(layer2){
            var precKey = layer2.feature.properties.PCTKEY;
            var currPrec = precinctKeys[precKey];
//            console.log('precKey '+precKey+' currPrec '+ currPrec);
            // console.log(layer2);
            layer2.setStyle({
                weight: 2,
                opacity: 1,
                color: 'white',
                dashArray: '3',
                fillOpacity: 0.7,
                fillColor: precinctDemoColor(selectedDemo, currPrec)
            });
        });
  });
//  alert('hkkgd');
})


electOpts.on('change',function(){
    // alert('afdas' + electionSetting);
    electionSetting = $("input[name='ElectionData']:checked").val();
    if(dicSize(electionDic[electionSetting]) == 0){
        getPrecinctData(electionSetting);
    }
    precLayer.eachLayer(function(layer){
          layer.eachLayer(function(layer2){
              var precKey = layer2.feature.properties.PCTKEY;
              var currPrec = electionDic[electionSetting][precKey];
  //            console.log('precKey '+precKey+' currPrec '+ currPrec);
              // console.log(layer2);
              layer2.setStyle({
                  weight: 2,
                  opacity: 1,
                  color: 'white',
                  dashArray: '3',
                  fillOpacity: 0.7,
                  fillColor: precinctColor(currPrec.Republican,currPrec.Democrat,currPrec.Green,currPrec.Libertarian)
              });
          });
    });
});
