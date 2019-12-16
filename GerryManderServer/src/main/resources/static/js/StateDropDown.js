//-------------------------------------------------------------------------------------------
L.StateSelect = {};
L.StateSelect.states = statesData.features;
L.StateSelect = L.Control.extend({
	options: {
		position: 'topright',
		title: 'State',
		states: L.StateSelect.states,
	},
	onAdd: function(map) {
		this.div = L.DomUtil.create('div','leaflet-stateselect-container');
		this.select = L.DomUtil.create('select','leaflet-stateselect',this.div);
		var content = '';
		if (this.options.title.length > 0 ){
			content += '<option>'+this.options.title+'</option>';
		}
		var states = (this.options.states);
		for (i in states){
				content+='<option>'+Object.values(Object.values(Object.values(Object.values(states)[i])[2]))[0]+'</option>';
		}
		this.select.innerHTML = content;
		this.select.onmousedown = L.DomEvent.stopPropagation;
		return this.div;
	},
	on: function(type,handler){
		if (type == 'change'){
			this.onChange = handler;
			L.DomEvent.addListener(this.select,'change',this._onChange,this);
		} else {
			console.log('StateSelect - cannot handle '+type+' events.')
		}
	},
	_onChange: function(e) {
		e.feature = this.options.states[this.select.selectedIndex-1];
		this.onChange(e);
	}
});

L.stateSelect = function(){
	return new L.StateSelect();
};

var select = L.stateSelect();
select.addTo(map);
// var data = L.geoJson(statesData);
select.on('change', function(e){
    // console.log(e);
	try{
		var state = L.geoJson(e.feature)
	    map.fitBounds(state.getBounds());

	    if(stateInit != 1){
				fetchDistrict(e);
			}
			else{
				districtData.show();
				stateData.show();
				stateData.update();
			}
			if(map.hasLayer(texas)){
	    	map.removeLayer(texas)
			}
        map.addLayer(texasDistrictsLayer);
//		map.on('drag', function() {
//			map.panInsideBounds(state.getBounds(), { animate: false });
//		});

		// console.log(stateData);
		outerMenuBtn.show();
		mapContent.show();
		// stateData.hide();
		// districtData.show();
		// if($('#districtContent').hasClass('active')){
		// 	districtData.show();
		// }
		// else{
		// 	precinctData.show();
		// }
		// $(".leaflet-control-zoom").css("display","block");


	}
	catch(error){
		recenterMap();
//		geojson.resetStyle(e.target);
//		stateData.update();
		if(map.hasLayer(precLayer)){
            map.removeLayer(precLayer);
        }
        if(map.hasLayer(texasDistrictsLayer)){
            map.removeLayer(texasDistrictsLayer);
        }
        if( $('#precinctContent').hasClass('active')){
						precinctData.hide();
            $('#districtContent').toggleClass('active');
            $('#precinctContent').toggleClass('active');
        }
		texas.addTo(map);
//		distLayer.clearLayers();
		// stateData.update();
		districtData.hide();
		precinctData.hide();
		stateData.hide();
		mapContent.hide();
		outerMenuBtn.hide()
		// console.log(body.attr('class'));
	  if(body.attr('class') == 'active-nav'){
	    body.toggleClass('active-nav');
	  }
		// console.log(body);
	}
});
