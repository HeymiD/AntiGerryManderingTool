// get color depending on population density value
function getColor(d) {
	// switch (feature.properties.party) {
  //           case 'Republican': return {color: "#ff0000"};
  //           case 'Democrat':   return {color: "#0000ff"};
  //       }
	return d > 1000 ? '#800026' :
				d > 500  ? '#BD0026' :
				d > 200  ? '#E31A1C' :
				d > 100  ? '#FC4E2A' :
				d > 50   ? '#FD8D3C' :
				d > 20   ? '#FEB24C' :
				d > 10   ? '#FED976' :
							     '#FFEDA0';
}

function style(feature) { //need to change the way we set colors based on ???
	return {
	  weight: 2,
		opacity: 1,
		color: 'white',
		dashArray: '3',
		fillOpacity: 0.7,
		fillColor: getColor(feature.properties.density)
	};
}

function onHover(e) {
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
  // stateData.update(layer.feature.properties);
}

function resetHighlight(e) {
	geojson.resetStyle(e.target);
	// stateData.update();
}

function zoomOnState(e) {
	$.ajax({
		url:"http://localhost:8080/state",
		data:{
			state: e.target.feature.properties.name,
			count:0
		},
		success: function(r){
			map.removeLayer(geojson);
			// console.log(r);
			var response=JSON.parse(r);
			// console.log("success");
			var texas_district = L.geoJson(response, {
				style: style,
				onEachFeature: stateEffects
			}).addTo(map);
			var count=1
			while(count<37){
				getDistricts(e,count);
				count=count+1;
			}
		 // console.log("success");
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
	stateData.update(e.target.feature.properties);
	stateData.show();
	// console.log(!outerMenuBtn.is(":visible
	// outerMenuBtn.show();
	if(body.attr('class') == 'active-nav'){
		outerMenuBtn.hide();
	}
	else{
		outerMenuBtn.show();
	}
}


function stateEffects(feature, layer) {
	layer.on({
		mouseover: onHover,
		mouseout: resetHighlight,
		click: zoomOnState
	});
}

function getDistricts(e,cnt){
	$.ajax({
		url:"http://localhost:8080/state",
		data: {
			state: e.target.feature.properties.name,
			count: cnt
		},
		success: function(districts){
			// console.log(disctricts);
			var response = JSON.parse(districts);
			// console.log("success");
			var texas_district = L.geoJson(districts, {
			    			style: style,
			    			onEachFeature: stateEffects
			    		});
			//            map.removeLayer(geojson);
			    map.addLayer(texas_district);
		},
		error:function(err){
		console.log(err)
		console.log("ERROR")
		}
	})
}

var geojson;
var texas;
var texas_precincts;


geojson = L.geoJson(statesData, {
			style: style,
			onEachFeature: stateEffects
		});

texas = L.geoJson(txStateData, {
	style: style,
	onEachFeature: stateEffects
}).addTo(map);
