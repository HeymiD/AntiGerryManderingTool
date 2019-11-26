// control that shows state info on hover
var stateData = L.control();
	stateData.onAdd = function (map) {
		this._div = L.DomUtil.create('div', 'selectedinfo');
		this._div.id = 'stateInfo';
		this.update();
		return this._div;
	};
	stateData.update = function (props) {
	  this._div.innerHTML = '<h4>State Data</h4>' +  (props ?
	  	'<b>' + props.name + '</b><br />' + props.density + ' people / mi<sup>2</sup>'
	  	: 'Select a state');
	};
	stateData.show = function(){
		$('#stateInfo').show();
	}
	stateData.hide = function(){
		$('#stateInfo').hide();
	}
	stateData.addTo(map);
	stateData.hide();

var districtData = L.control();
	districtData.onAdd = function (map) {
		this._div = L.DomUtil.create('div', 'selectedinfo');
		this._div.id = 'districtInfo';
		this.update();
		return this._div;
	};
	districtData.update = function (props) {
	  this._div.innerHTML = '<h4>District Data</h4>' +  (props ?
	  	'<b>' + props.name + '</b><br />' + props.density + ' people / mi<sup>2</sup>'
	  	: 'Hover over a District');
	};
	districtData.show = function(){
		$('#districtInfo').show();
	}
	districtData.hide = function(){
		$('#districtInfo').hide();
	}
	districtData.addTo(map);

var precintData = L.control();
	precintData.onAdd = function (map) {
		this._div = L.DomUtil.create('div', 'selectedinfo');
		this._div.id = 'precinctInfo';
		this.update();
		return this._div;
	};
	precintData.update = function (props) {
	  this._div.innerHTML = '<h4>Precinct Data</h4>' +  (props ?
	  	'<b>' + props.name + '</b><br />' + props.density + ' people / mi<sup>2</sup>'
	  	: 'Hover over a precinct');
	};
	precintData.show = function(){
		$('#precinctInfo').show();
	}
	precintData.hide = function(){
		$('#precinctInfo').hide();
	}
	precintData.addTo(map);
