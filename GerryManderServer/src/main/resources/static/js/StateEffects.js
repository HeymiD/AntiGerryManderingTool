var usCenter = [37.8, -96];
var texas_precincts;
function recenterMap(){
	map.setView(usCenter,5);
}
function getColor(d) {
	// switch (feature.properties.party) {
  //           case 'Republican': return {color: "#ff0000"};
  //           case 'Democrat':   return {color: "#0000ff"};
  //       };
	return d > 10 ? '#ffb0a0' :
				 d > 5  ? '#a0d4ff' :
							     		  '#d7ffa0';
}

function style(feature) { //need to change the way we set colors based on ???
	return {
	  weight: 2,
		opacity: 1,
		color: 'white',
		dashArray: '3',
		fillOpacity: 0.7,
		fillColor: getColor(feature.properties.COLOR)
	};
}

function onStateHover(e) {
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
  stateData.update(layer.feature.properties);
}

function stateMouseOut(e) {
	geojson.resetStyle(e.target);
	stateData.update();
}

function zoomOnState(e) {

	$.ajax({
		url:"http://localhost:8080/state",
		data:{
			state: e.target.feature.properties.name,
			districtId:1
		},
		success: function(response){
			map.removeLayer(geojson);
			var districtFirst=JSON.parse(response);
			var districtLayer = L.geoJson(districtFirst, {
				style: style,
				onEachFeature: stateEffects
			})
//			map.addLayer(districtLayer);
//			var districtId=2
//			while(districtId<37){
//				getDistrict(e,districtId);
//				districtId=districtId+1;
//			}
			var done=0
			while(done==0){
			    getPrecincts(e,done)
			}
		},
		error:function(err){
			console.log(err, "ERROR");
		}
	})
	map.fitBounds(e.target.getBounds());
	map.on('drag', function() {
	  map.panInsideBounds(e.target.getBounds(), { animate: false });
	});
	select.select.selectedIndex = 1;
	mapContent.show();
	// console.log(!outerMenuBtn.is(":visible
	// outerMenuBtn.show();
	if(body.attr('class') == 'active-nav'){
		outerMenuBtn.hide();

	}
	else{
		outerMenuBtn.show();
	}
	stateData.hide();
	districtData.show();
}

function stateEffects(feature, layer) {
	layer.on({
		mouseover: onStateHover,
		mouseout: stateMouseOut,
		click: zoomOnState
	});
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
  districtData.update(layer.feature.properties);
}

function districtMouseOut(e) {
	geojson.resetStyle(e.target);
	districtData.update();
}

function districtEffects(feature, layer) {
	layer.on({
		mouseover: onDistrictHover,
		mouseout: districtMouseOut,
		click: zoomOnState
	});
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
	geojson.resetStyle(e.target);
	precinctData.update();
}

function precinctEffects(feature, layer) {
	layer.on({
		mouseover: onPrecinctHover,
		mouseout: precinctMouseOut,
		click: zoomOnState
	});
}

function getDistrict(e,districtId){
	$.ajax({
		url:"http://localhost:8080/state",
		data: {
			state: e.target.feature.properties.name,
			districtId: districtId
		},
		success: function(response){
			// console.log(response);
			var district = JSON.parse(response);
			var districtLayer = L.geoJson(district, {
			    			style: style,
			    			onEachFeature: stateEffects
			    		});
			    map.addLayer(districtLayer);
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

function getPrecincts(e,done){
	$.ajax({
		url:"http://localhost:8080/precincts",
		data: {
			state: e.target.feature.properties.name
		},
		success: function(response){
		    if(response=="done"){
		        done=1
		        return
		    }
			// console.log(response);
			var precinct = JSON.parse(response);
			var precinctLayer = L.geoJson(precinct, {
			    			style: style,
			    			onEachFeature: stateEffects
			    		});
			    map.addLayer(precinctLayer);
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

// function getDistricts(e,cnt){
// 	$.ajax({
// 		url:"http://localhost:8080/state",
// 		data: {
// 			state: e.target.feature.properties.name,
// 			count: cnt
// 		},
// 		success: function(districts){
// 			// console.log(disctricts);
// 			var response = JSON.parse(districts);
// 			// console.log("success");
// 			var texas_district = L.geoJson(districts, {
// 			    			style: style,
// 			    			onEachFeature: stateEffects
// 			    		});
// 			//            map.removeLayer(geojson);
// 			    map.addLayer(texas_district);
// 		},
// 		error:function(err){
// 		console.log(err)
// 		console.log("ERROR")
// 		}
// 	})
// }






// function zoomOnState(e) {
// 	$.ajax({
// 		url:"http://localhost:8080/state",
// 		data:{
// 			state: e.target.feature.properties.name,
// 			count:0
// 		},
// 		success: function(r){
// 			map.removeLayer(geojson);
// 			// console.log(r);
// 			var response=JSON.parse(r);
// 			// console.log("success");
// 			var texas_district = L.geoJson(response, {
// 				style: style,
// 				onEachFeature: stateEffects
// 			}).addTo(map);
// 			var count=1
// 			while(count<37){
// 				getDistricts(e,count);
// 				count=count+1;
// 			}
// 		 // console.log("success");
// 		},
// 		error:function(err){
// 			console.log(err, "ERROR");
// 		}
// 	})
// 	map.fitBounds(e.target.getBounds());
// 	map.on('drag', function() {
// 	  map.panInsideBounds(e.target.getBounds(), { animate: false });
// 	});
// 	select.select.selectedIndex = 1;
// 	mapContent.show();
// 	// console.log(!outerMenuBtn.is(":visible
// 	// outerMenuBtn.show();
// 	if(body.attr('class') == 'active-nav'){
// 		outerMenuBtn.hide();
// 
// 	}
// 	else{
// 		outerMenuBtn.show();
// 	}
// 	stateData.hide();
// 	districtData.show();
// }
