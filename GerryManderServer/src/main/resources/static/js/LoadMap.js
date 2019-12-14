var outerMenuBtn = $('#outer-menu-btn');
var innerMenuBtn = $('#inner-menu-btn');
var mapContent = $('#selectMapContent');
var body = $('body');
var nav = $('nav');
var bounds = [[20, -150],[55, -40]];
var geojson;
var texas;
var texasDistrictsLayer;
//var texasPrecinctsLayer;


var map = L.map('map', {
		center: [37.8, -96],
		zoom: 5,
		maxBounds: bounds,
		zoomControl: false,
		doubleClickZoom: false,
		scrollWheelZoom: false,
		touchZoom: false,
		inertia: false
});
//map.zoomControl.setPosition('bottomright');
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
	maxZoom: 18, minZoom: 5,
	attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
	'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
	'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
	id: 'mapbox.light'
}).addTo(map);

geojson = L.geoJson(statesData, {
	style: stateStyle,
	onEachFeature: stateEffects
});

texas = L.geoJson(txStateData, {
	style: stateStyle,
	onEachFeature: stateEffects
}).addTo(map);

texasDistrictsLayer =  L.geoJson(texas_districts, {
                  	style: districtStyle,
                  	onEachFeature: districtEffects
                  });
loadjscssfile("./js/geojson/PrecinctsPart3.js", "js",function (){
                                                             console.log(precincts3)
                                                             var precinctBoundary = L.geoJson(precincts3, {
                                                             	style: precinctStyle,
                                                             	onEachFeature: precinctEffects
                                                             })
                                                             precLayer.addLayer(precinctBoundary)
                                                         })
loadjscssfile("./js/geojson/PrecinctsPart4.js", "js",function (){
                                                         console.log(precincts4)
                                                         var precinctBoundary = L.geoJson(precincts4, {
                                                            style: precinctStyle,
                                                            onEachFeature: precinctEffects
                                                         })
                                                         precLayer.addLayer(precinctBoundary)
                                                     })

//texasPrecinctsLayer = L.geoJson(texas_precintcs, {
//                                        	style: stateStyle,
//                                        	onEachFeature: stateEffects
//                                        });

recenterMap();
outerMenuBtn.hide();
mapContent.hide();

map.on('drag', function() {
	map.panInsideBounds(bounds, { animate: false });
});
