var usCenter = [37.8, -96];
var texas_precincts;
function recenterMap(){
	map.setView(usCenter,5);
}
function getColor(d) {
	// switch (feature.properties.party) {
  //           case 'Republican': return {color: "#ff0000"};
  //           case 'Democrat':   return {color: "#0000ff"};
  //       }
	d = parseInt(d.replace(/[,]/g,""));
	return d > 10000000 ? '#ffb0a0' :
				 d > 5000000  ? '#a0d4ff' :
							     		  '#d7ffa0';
}

function style(feature) { //need to change the way we set colors based on ???
	return {
	  weight: 2,
		opacity: 1,
		color: 'white',
		dashArray: '3',
		fillOpacity: 0.7,
		fillColor: getColor(feature.properties.population)
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

function stateEffects(feature, layer) {
	layer.on({
		mouseover: onHover,
		mouseout: resetHighlight,
		click: zoomOnState
	});
}
