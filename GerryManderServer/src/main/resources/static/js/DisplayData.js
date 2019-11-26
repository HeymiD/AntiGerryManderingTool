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
	  	'<b>' + props.name + '</b><br />Population: ' + props.population
	  	: 'Select a state');
	};
	stateData.show = function(){
		$('#stateInfo').show();
	}
	stateData.hide = function(){
		$('#stateInfo').hide();
	}

	stateData.addTo(map);
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
	districtData.hide();

var precinctData = L.control();
	precinctData.onAdd = function (map) {
		this._div = L.DomUtil.create('div', 'selectedinfo');
		this._div.id = 'precinctInfo';
		this.update();
		return this._div;
	};
	precinctData.update = function (props) {
	  this._div.innerHTML = '<h4>Precinct Data</h4>' +  (props ?
	  	'<b>' + props.name + '</b><br />' + props.density + ' people / mi<sup>2</sup>'
	  	: 'Hover over a precinct');
	};
	precinctData.show = function(){
		$('#precinctInfo').show();
	}
	precinctData.hide = function(){
		$('#precinctInfo').hide();
	}
	precinctData.addTo(map);
	precinctData.hide();


function selMapContent(content){
	var thisContent;
	var otherContent;
	switch(content){
		case 'districtContent':
		thisContent = $('#districtContent');
		otherContent = $('#precinctContent');
		districtData.show();
		precinctData.hide();
			break;
		case 'precinctContent':
			thisContent = $('#precinctContent');
			otherContent = $('#districtContent');
			districtData.hide();
			precinctData.show();
			break;
	}
	if(!thisContent.hasClass('active')){
		thisContent.toggleClass('active');
		otherContent.toggleClass('active');

	}
}

var phaseData = L.control();
	phaseData.options.position = 'bottomright';
	phaseData.onAdd = function (map) {
		this._div = L.DomUtil.create('div', 'selectedinfo');
		this._div.id = 'phase0info';
		this.update();
		return this._div;
	};
	phaseData.update = function (props) {
	  this._div.innerHTML = '<h4>Majority Minority Precincts</h4>' + '<b>Num of Precincts: </b>' + '<br/><b>Num of Maj-Min Precincts: </b>'
		+ '<br/><b>% of {insert selected race here} Majority: </b>' + '<br/><b>% of {insert selected race here} Majority: </b>' ;
	};
	phaseData.show = function(){
		$('#phase0info').show();
	}
	phaseData.hide = function(){
		$('#phase0info').hide();
	}
	phaseData.addTo(map);
	phaseData.hide();



$('#phaseType').change(function(){
	if($('#phaseType').val() == 'Phase 0'){
		phaseData.show();
	}
	else{
		phaseData.hide();
	}
});
