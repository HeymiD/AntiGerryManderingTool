var usCenter = [37.8, -96];
var currState;
var texas_precincts;
var distPrecinct = {}
var districtx = {}
var districtxData = {}
var distLayer = L.layerGroup();
var precLayer = L.layerGroup();
var disColor = ['#3078dd','#47c871','#72b189','#0de85a','#6d73c4','#57ab0f','#3078dd','#5186af','#3fa20b','#a490a0',
                '#71aabe','#1940fe','#5bc4a3','#9a4d46','#866dc6','#80f4d0','#b0c7d7','#137a9d','#4d6d3a','#388f7a',
                '#bfb119','#1e5c1e','#3d9ed7','#518d92','#420f26','#b81163','#973a02','#413db1','#00718e','#08e548',
                '#191760','#c82d7d','#cdde56','#88670d','#612d87','#371b95','#eb95e9']

function dicSize(x){
    var  len = 0;
    for (var item in x){
        len ++;
    }
    return len;
}

function fetchDistrict(e){
    if(dicSize(districtx) == 0){
        $.ajax({
            url:"http://localhost:8080/state",
            data:{
                state: e.feature.properties.name,
                districtId:1
            },
            success: function(response){
                var districtFirst=JSON.parse(response);
                var districtLayer = L.geoJson(districtFirst, {
                    style: districtStyle,
                    onEachFeature: districtEffects
                })
                districtx[1]=districtLayer
                districtLayer.addTo(distLayer);
                var districtId=2
                while(districtId<37){
                    getDistrict(e,districtId);
                    districtId=districtId+1;
                }

            },
            error:function(err){
                console.log(err, "ERROR");
            }
        })

    }
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
//			    map.addLayer(districtLayer);
			    districtx[districtId] = districtLayer;
                if(distLayer.getLayers().length == 36 ){
                    $('#precinctContent').prop('disabled', false);
                }
//			if(districtId==36){
//			districtID=1
//                while(districtID<37){
////                    console.log("District: "+districtID)
//                    getPrecincts(e,districtID);
//                    districtID=districtID+1;
//                }
//			}
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

function getPrecincts(e,districtId,electType){
	$.ajax({
		url:"http://localhost:8080/precincts",
		data: {
			state: e.feature.properties.name,
			districtId: districtId,
			electType: electType
		},
		success: function(response){
//			 console.log(response);
			var precinct = JSON.parse(response);
			var precinctLayer = L.geoJson(precinct, {
			    			style: precinctStyle,
			    			onEachFeature: precinctEffects
			    		});
//			    map.addLayer(precinctLayer);
                precinctLayer.addTo(precLayer);
                distPrecinct[districtId] = precinctLayer;
//            console.log('distID', districtId, 'curr stuff = ', distPrecinct);
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

function recenterMap(){ map.setView(usCenter,5); }

function getDistrictData(districtId,elecType){
$.ajax({
		url:"http://localhost:8080/districtData",
		data: {
			districtId: districtId,
			elecType: elecType
		},
		success: function(response){
		console.log(response)
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

//==================== State Zone ====================

function stateColor(d){
	return d > 3 ? '#ffb0a0' : d > 12  ? '#a0d4ff' : '#d7ffa0';
}

function stateStyle(feature){ //need to change the way we set colors based on ???
	return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
        fillColor: stateColor(feature.properties.COLOR)
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
    stateData.update(layer.feature.properties);
}

function stateMouseOut(e) {
	geojson.resetStyle(e.target);
	stateData.update();
}

function zoomOnState(e) {

    console.log(e);
    fetchDistrict(e.target);
    currState = e.target;
//    map.removeLayer(geojson);
    map.removeLayer(texas)
    map.addLayer(distLayer);
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
//	districtData.show();
}

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
    if(distLayer.getLayers().length==36 && electionType.val() != 'Election Type' && electionYear.val() != 'Election Year'){
        console.log("Getting district data")
        var districtId = "U.S. Rep "+layer.feature.properties.fid
        if(!(districtId in districtxData)){
    //        console.log('electionType.val()+electionYear.val() = '+ electionType.val() + electionYear.val())
            getDistrictData(districtId, electionType.val()+electionYear.val());}
        else{
            districtData.update(districtxData[districtId])
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
		click: zoomOnDistrict
	});
}

//==================== Precinct Zone ====================
function precinctColor(r, d, g, l){
    var power = Math.max(r,d,g,l)
	return r == power ? '#E9141D' : d == power ? '#0015BC' : g == power  ? '#00ff00 ' : '#FED105';
}

function precinctStyle(feature){
    return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
        fillColor: precinctColor(feature.properties.Republican, feature.properties.Democrat, feature.properties.Green, feature.properties.Libertarian)
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
  precinctData.update(layer.feature.properties);
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
		click: zoomOnPrecinct
	});
}
