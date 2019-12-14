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
	  	'<b>' +'District ID: '+props.DistrictID + '</b><br />'
	  	+ 'Population: '+props.Population + '</b><br />'
	  	+ 'White: ' + props.White + '</b><br />'
	  	+ 'Black: ' +  props.Black + '</b><br />'
	  	+ 'Hispanic: ' + props.Hispanic + '</b><br />'
	  	+ 'Pacific: ' + props.Pacific + '</b><br />'
	  	+ 'Native: ' + props.Native + '</b><br />'
	  	+ 'Asian: ' + props.Asian + '</b><br />'
	  	+ 'Other: ' + props.Other + '</b><br />'
	  	+ 'Republican: ' + props.Republican + '</b><br />'
	  	+ 'Democrat: ' + props.Democrat + '</b><br />'
	  	+ 'Green: ' + props.Green + '</b><br />'
	  	+ 'Libertarian: ' + props.Libertarian + '</b><br />'
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
	  	'<b>PrecinctID: </b>' + props.PrecinctID +
	  	'<b><br />DistrictID: </b>' + props.DistrictID +
	  	'<b><br />Republican Votes: </b>' + props.Republican +
	  	'<b><br />Democrat Votes: </b>' + props.Democrat +
	  	'<b><br />Green Votes: </b>' + props.Green +
	  	'<b><br />Libertarian Votes: </b>' + props.Libertarian +
	  	'<b><br />White Population: </b>' + props.White +
	  	'<b><br />Black Population: </b>' + props.Black +
	  	'<b><br />Hispanic Population: </b>' + props.Hispanic +
	  	'<b><br />Native Population: </b>' + props.Native +
	  	'<b><br />Pacific Population: </b>' + props.Pacific +
	  	'<b><br />Asian Population: </b>' + props.Asian +
	  	'<b><br />Other Population: </b>' + props.Other
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
    //		if(map.hasLayer(precLayer)){
    //        	map.removeLayer(precLayer);
    //        }
            if(map.hasLayer(texas)){
                map.removeLayer(texas);
            }
    //        if(!map.hasLayer(distLayer)){
    //        	map.addLayer(distLayer);
    //        }
//            for (var districtId in districtx){
//                var district = districtx[districtId];
//                var precinct = precinctData[districtId];
//                if(map.hasLayer(precLayer)){
//                    map.removeLayer(precLayer);
//                    }

                if(!map.hasLayer(texasDistrictsLayer)){
                    map.addLayer(texasDistrictsLayer);
                }
//            }

            if(electionYear.val() == 'Election Year' && electionType.val() == 'Election Type'){
                districtData.hide();
            }
            else{
                districtData.show();
            }
            precinctData.hide();
            if(!thisContent.hasClass('active')){
                thisContent.toggleClass('active');
                otherContent.toggleClass('active');
            }
			break;
		case 'precinctContent':
			thisContent = $('#precinctContent');
			otherContent = $('#districtContent');
			if(map.hasLayer(texas)){
                map.removeLayer(texas);
            }
            if(electionYear.val() == 'Election Year' || electionType.val() == 'Election Type'){
                alert('Please Select an Election Type and Year in the Menu');

            }
            else{
//                for (var districtId in districtx){
//                    var district = districtx[districtId];
//                    var precinct = precinctData[districtId];
                    if(map.hasLayer(texasDistrictsLayer)){
                        map.removeLayer(texasDistrictsLayer);
                        }

                    if(!map.hasLayer(precLayer)){
                        map.addLayer(precLayer);
                    }
//                }
                districtData.hide();
                precinctData.show();
                if(precLayer.getLayers().length == 0 && electionYear.val() != 'Election Year' && electionType.val() != 'Election Type'){
                    console.log("Getting Precincts...")
                    loadjscssfile("./js/geojson/PrecinctsPart1.js", "js",function (){
                                                                                 console.log(precincts1)
                                                                                 var precinctBoundary = L.geoJson(precincts1, {
                                                                                 	style: precinctStyle,
                                                                                 	onEachFeature: precinctEffects
                                                                                 }).addTo(map)
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart2.js", "js",function (){
                                                                             console.log(precincts2)
                                                                             var precinctBoundary = L.geoJson(precincts2, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart3.js", "js",function (){
                                                                                 console.log(precincts3)
                                                                                 var precinctBoundary = L.geoJson(precincts3, {
                                                                                 	style: precinctStyle,
                                                                                 	onEachFeature: precinctEffects
                                                                                 }).addTo(map)
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart4.js", "js",function (){
                                                                             console.log(precincts4)
                                                                             var precinctBoundary = L.geoJson(precincts4, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart5.js", "js",function (){
                                                                                     console.log(precincts5)
                                                                                     var precinctBoundary = L.geoJson(precincts5, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart6.js", "js",function (){
                                                                             console.log(precincts6)
                                                                             var precinctBoundary = L.geoJson(precincts6, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart7.js", "js",function (){
                                                                                     console.log(precincts7)
                                                                                     var precinctBoundary = L.geoJson(precincts7, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart8.js", "js",function (){
                                                                             console.log(precincts8)
                                                                             var precinctBoundary = L.geoJson(precincts8, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart9.js", "js",function (){
                                                                                     console.log(precincts9)
                                                                                     var precinctBoundary = L.geoJson(precincts9, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart10.js", "js",function (){
                                                                             console.log(precincts10)
                                                                             var precinctBoundary = L.geoJson(precincts10, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart11.js", "js",function (){
                                                                                 console.log(precincts11)
                                                                                 var precinctBoundary = L.geoJson(precincts11, {
                                                                                    style: precinctStyle,
                                                                                    onEachFeature: precinctEffects
                                                                                 }).addTo(map)
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart12.js", "js",function (){
                                                                             console.log(precincts12)
                                                                             var precinctBoundary = L.geoJson(precincts12, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart13.js", "js",function (){
                                                                                     console.log(precincts13)
                                                                                     var precinctBoundary = L.geoJson(precincts13, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart14.js", "js",function (){
                                                                             console.log(precincts14)
                                                                             var precinctBoundary = L.geoJson(precincts14, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart15.js", "js",function (){
                                                                                     console.log(precincts15)
                                                                                     var precinctBoundary = L.geoJson(precincts15, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart16.js", "js",function (){
                                                                             console.log(precincts16)
                                                                             var precinctBoundary = L.geoJson(precincts16, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart17.js", "js",function (){
                                                                                     console.log(precincts17)
                                                                                     var precinctBoundary = L.geoJson(precincts17, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart18.js", "js",function (){
                                                                             console.log(precincts18)
                                                                             var precinctBoundary = L.geoJson(precincts18, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart19.js", "js",function (){
                                                                                 console.log(precincts19)
                                                                                 var precinctBoundary = L.geoJson(precincts19, {
                                                                                    style: precinctStyle,
                                                                                    onEachFeature: precinctEffects
                                                                                 }).addTo(map)
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart20.js", "js",function (){
                                                                             console.log(precincts20)
                                                                             var precinctBoundary = L.geoJson(precincts20, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart21.js", "js",function (){
                                                                                     console.log(precincts21)
                                                                                     var precinctBoundary = L.geoJson(precincts21, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart22.js", "js",function (){
                                                                             console.log(precincts22)
                                                                             var precinctBoundary = L.geoJson(precincts22, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart23.js", "js",function (){
                                                                                     console.log(precincts23)
                                                                                     var precinctBoundary = L.geoJson(precincts23, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart24.js", "js",function (){
                                                                             console.log(precincts24)
                                                                             var precinctBoundary = L.geoJson(precincts24, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart25.js", "js",function (){
                                                                                     console.log(precincts25)
                                                                                     var precinctBoundary = L.geoJson(precincts25, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart26.js", "js",function (){
                                                                             console.log(precincts26)
                                                                             var precinctBoundary = L.geoJson(precincts26, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart27.js", "js",function (){
                                                                                 console.log(precincts27)
                                                                                 var precinctBoundary = L.geoJson(precincts27, {
                                                                                    style: precinctStyle,
                                                                                    onEachFeature: precinctEffects
                                                                                 }).addTo(map)
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart28.js", "js",function (){
                                                                             console.log(precincts28)
                                                                             var precinctBoundary = L.geoJson(precincts28, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart29.js", "js",function (){
                                                                                     console.log(precincts29)
                                                                                     var precinctBoundary = L.geoJson(precincts29, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart30.js", "js",function (){
                                                                             console.log(precincts30)
                                                                             var precinctBoundary = L.geoJson(precincts30, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart31.js", "js",function (){
                                                                                     console.log(precincts31)
                                                                                     var precinctBoundary = L.geoJson(precincts31, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart32.js", "js",function (){
                                                                             console.log(precincts32)
                                                                             var precinctBoundary = L.geoJson(precincts32, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             }).addTo(map)
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })

//                    map.addLayer(precLayer)
//                    getPrecincts(currState)
//                    var districtID = 1
//                    while(districtID<37){
//                        console.log("getting Precincts now")
//                        getPrecincts(currState,districtID);
////                        getPrecinctsByDistrictInit(currState,districtID)
//                        console.log("Got precinct for district id: " + districtID)
//                        districtID=districtID+1;
//                    }

//                    var done=0;
//                    while(done<8936){
//                        console.log("getting Precincts now")
//                        getPrecincts(currState)
//                        done=done+40
//                    }

//                    for (pctkey in precinctKeys.PrecinctKeys) {
//                        console.log("Precinct: "+pctkey);
//                        getPrecincts(currState,precinctKeys.PrecinctKeys[pctkey])
//                    }

                }
                if(!thisContent.hasClass('active')){
                    thisContent.toggleClass('active');
                    otherContent.toggleClass('active');
                }
            }
			break;

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
		+ '<br/><b>% of {insert selected race here} voted as a bloc: </b>' + '<br/><b>% of {insert selected race here} voted as bloc: </b>' ;
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
