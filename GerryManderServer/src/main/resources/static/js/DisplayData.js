// control that shows state info on hover

var stateData = L.control();
	stateData.onAdd = function (map) {
		this._div = L.DomUtil.create('div', 'selectedinfo');
		this._div.id = 'stateInfo';
//		this.update();
		return this._div;
	};
	stateData.update = function () {
        calcPrecPops();
        this._div.innerHTML = '<h4>State Data</h4>'
                            + '<b>State Name: </b>' + currState
                            + '</br><b>Total Population: </b>' + numberWithCommas(totPopulation)
                            + '</br><b>White: </b>' + ((totWhite/totPopulation)*100).toFixed(2)
                            + '%</br><b>Black: </b>' + ((totBlack/totPopulation)*100).toFixed(2)
                            + '%</br><b>Hispanic: </b>' + ((totHispanic/totPopulation)*100).toFixed(2)
                            + '%</br><b>Pacific: </b>' + ((totPacific/totPopulation)*100).toFixed(2)
                            + '%</br><b>Native: </b>' + ((totNative/totPopulation)*100).toFixed(2)
                            + '%</br><b>Asian: </b>' + ((totAsian/totPopulation)*100).toFixed(2)
                            + '%</br><b>Other: </b>' + ((totOther/totPopulation)*100).toFixed(2) +'%'
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
	districtData.update = function (props,distId) {

	  this._div.innerHTML = '<h4>District Data</h4>' +  (props ?
	  	'<b> District ID: </b>'+ (props.DistrictID) + '<br />'
	  	+ '<b> Population: </b>'+numberWithCommas(props.Population) + '<br />'
	  	+ '<b> White: </b> ' + ((props.White/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Black: </b> ' +  ((props.Black/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Hispanic: </b> ' + ((props.Hispanic/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Pacific: </b> ' + ((props.Pacific/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Native: </b> ' + ((props.Native/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Asian: </b> ' + ((props.Asian/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Other: </b> ' + ((props.Other/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Republican: </b> ' + ((props.Republican/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Democrat: </b> ' + ((props.Democrat/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Green: </b> ' + ((props.Green/props.Population)*100).toFixed(2) + '%<br />'
	  	+ '<b> Libertarian: </b> ' + ((props.Libertarian/props.Population)*100).toFixed(2) + '%<br />'
			+ '<b> Incumbent: </b> ' + incumbs[parseInt(distId)]
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
	precinctData.update = function (props,elect,precKey) {
	  this._div.innerHTML = '<h4>Precinct Data</h4>' +  (props ?
	  	'<b>PrecinctID: </b>' + precKey +
	  	'<b><br />DistrictID: </b>' + numberWithCommas(props.DistrictID) +
	  	'<b><br />Republican Votes: </b>' + numberWithCommas(elect.Republican) +
	  	'<b><br />Democrat Votes: </b>' + numberWithCommas(elect.Democrat) +
	  	'<b><br />Green Votes: </b>' + numberWithCommas(elect.Green) +
	  	'<b><br />Libertarian Votes: </b>' + numberWithCommas(elect.Libertarian) +
	  	'<b><br />White Population: </b>' + numberWithCommas(props.White) +
	  	'<b><br />Black Population: </b>' + numberWithCommas(props.Black) +
	  	'<b><br />Hispanic Population: </b>' + numberWithCommas(props.Hispanic) +
	  	'<b><br />Native Population: </b>' + numberWithCommas(props.Native) +
	  	'<b><br />Pacific Population: </b>' + numberWithCommas(props.Pacific) +
	  	'<b><br />Asian Population: </b>' + numberWithCommas(props.Asian) +
	  	'<b><br />Other Population: </b>' + numberWithCommas(props.Other)
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




function updatePhase0(props){
    // console.log('im in updatePhase0');
	var phaseData = $('#Phase0-Data');
	phaseData.html(
	'<b># of Precincts: </b>' + numberWithCommas(props.numPrecincts)
	+ '<br/><b># Majority Minority: </b>' + numberWithCommas(props.numMajMinPrecincts)

	+ '<br/><br/><h3>Voting Blocs by Demographic</h3>'
	+ '<b>Black: </b>' + numberWithCommas(props.Black.toFixed(2))
	+ '%<br/><b>Hispanic: </b>' + props.Hispanic.toFixed(2)
	+ '%<br/><b>Pacific: </b>' + props.Pacific.toFixed(2)
	+ '%<br/><b>Asian: </b>' + props.Asian.toFixed(2)
	+ '%<br/><b>Native: </b>' + props.Native.toFixed(2)
	+ '%<br/><b>Other: </b>' + props.Other.toFixed(2)

	+ '%<br/><br/><h3>Voting Blocs by Party</h3>'
	+ '<b>Democrat: </b>' + props.Democrat.toFixed(2)
	+ '%<br/><b>Republican: </b>' + props.Republican.toFixed(2)
	+ '%<br/><b>Green: </b>' + props.Green.toFixed(2)
	+ '%<br/><b>Libertarian: </b>' + props.Libertarian.toFixed(2) + '%'

);
}

function calcPrecPops(){
	totWhite = totBlack = totHispanic = totPacific = totAsian = totNative = totOther = totPopulation = 0;
	for(var precId in precinctKeys){
        if(precId != null){
            // console.log(precId);
            totWhite += parseInt(precinctKeys[precId].White);
            totBlack += parseInt(precinctKeys[precId].Black);
            totHispanic += parseInt(precinctKeys[precId].Hispanic);
            totPacific += parseInt(precinctKeys[precId].Pacific);
            totAsian += parseInt(precinctKeys[precId].Asian);
            totNative += parseInt(precinctKeys[precId].Native);
            totOther += parseInt(precinctKeys[precId].Other);
        }
	}
	totPopulation = totWhite + totBlack + totHispanic + totPacific + totAsian + totNative + totOther;
	// console.log(totPopulation);
}




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
      if(map.hasLayer(texas)){ map.removeLayer(texas); }
			if(map.hasLayer(precLayer)){
				map.removeLayer(precLayer);
				// precLayer = L.layerGroup();
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

      if(!map.hasLayer(texasDistrictsLayer)){ map.addLayer(texasDistrictsLayer); }
//            }

      precinctData.hide();
			districtData.show();
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
								if(!thisContent.hasClass('active')){
										thisContent.toggleClass('active');
										otherContent.toggleClass('active');
								}
                districtData.hide();
                precinctData.show();
                if(precLayer.getLayers().length == 0){
                    // console.log("Getting Precincts...")

										loadjscssfile("./js/geojson/PrecinctsPart1.js", "js",function (){
                                                                                 // console.log(precincts1)
                                                                                 var precinctBoundary = L.geoJson(precincts1, {
                                                                                 	style: precinctStyle,
                                                                                 	onEachFeature: precinctEffects
                                                                                 })
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart2.js", "js",function (){
                                                                             // console.log(precincts2)
                                                                             var precinctBoundary = L.geoJson(precincts2, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart3.js", "js",function (){
                                                                                 // console.log(precincts3)
                                                                                 var precinctBoundary = L.geoJson(precincts3, {
                                                                                 	style: precinctStyle,
                                                                                 	onEachFeature: precinctEffects
                                                                                 })
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart4.js", "js",function (){
                                                                             // console.log(precincts4)
                                                                             var precinctBoundary = L.geoJson(precincts4, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart5.js", "js",function (){
                                                                                     // console.log(precincts5)
                                                                                     var precinctBoundary = L.geoJson(precincts5, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart6.js", "js",function (){
                                                                             // console.log(precincts6)
                                                                             var precinctBoundary = L.geoJson(precincts6, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart7.js", "js",function (){
                                                                                     // console.log(precincts7)
                                                                                     var precinctBoundary = L.geoJson(precincts7, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart8.js", "js",function (){
                                                                             // console.log(precincts8)
                                                                             var precinctBoundary = L.geoJson(precincts8, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart9.js", "js",function (){
                                                                                     // console.log(precincts9)
                                                                                     var precinctBoundary = L.geoJson(precincts9, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart10.js", "js",function (){
                                                                             // console.log(precincts10)
                                                                             var precinctBoundary = L.geoJson(precincts10, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart11.js", "js",function (){
                                                                                 // console.log(precincts11)
                                                                                 var precinctBoundary = L.geoJson(precincts11, {
                                                                                    style: precinctStyle,
                                                                                    onEachFeature: precinctEffects
                                                                                 })
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart12.js", "js",function (){
                                                                             // console.log(precincts12)
                                                                             var precinctBoundary = L.geoJson(precincts12, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart13.js", "js",function (){
                                                                                     // console.log(precincts13)
                                                                                     var precinctBoundary = L.geoJson(precincts13, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart14.js", "js",function (){
                                                                             // console.log(precincts14)
                                                                             var precinctBoundary = L.geoJson(precincts14, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart15.js", "js",function (){
                                                                                     // console.log(precincts15)
                                                                                     var precinctBoundary = L.geoJson(precincts15, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart16.js", "js",function (){
                                                                             // console.log(precincts16)
                                                                             var precinctBoundary = L.geoJson(precincts16, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart17.js", "js",function (){
                                                                                     // console.log(precincts17)
                                                                                     var precinctBoundary = L.geoJson(precincts17, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart18.js", "js",function (){
                                                                             // console.log(precincts18)
                                                                             var precinctBoundary = L.geoJson(precincts18, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart19.js", "js",function (){
                                                                                 // console.log(precincts19)
                                                                                 var precinctBoundary = L.geoJson(precincts19, {
                                                                                    style: precinctStyle,
                                                                                    onEachFeature: precinctEffects
                                                                                 })
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart20.js", "js",function (){
                                                                             // console.log(precincts20)
                                                                             var precinctBoundary = L.geoJson(precincts20, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart21.js", "js",function (){
                                                                                     // console.log(precincts21)
                                                                                     var precinctBoundary = L.geoJson(precincts21, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart22.js", "js",function (){
                                                                             // console.log(precincts22)
                                                                             var precinctBoundary = L.geoJson(precincts22, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart23.js", "js",function (){
                                                                                     // console.log(precincts23)
                                                                                     var precinctBoundary = L.geoJson(precincts23, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart24.js", "js",function (){
                                                                             // console.log(precincts24)
                                                                             var precinctBoundary = L.geoJson(precincts24, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart25.js", "js",function (){
                                                                                     // console.log(precincts25)
                                                                                     var precinctBoundary = L.geoJson(precincts25, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     })
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart26.js", "js",function (){
                                                                             // console.log(precincts26)
                                                                             var precinctBoundary = L.geoJson(precincts26, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart27.js", "js",function (){
                                                                                 // console.log(precincts27)
                                                                                 var precinctBoundary = L.geoJson(precincts27, {
                                                                                    style: precinctStyle,
                                                                                    onEachFeature: precinctEffects
                                                                                 })
                                                                                 precLayer.addLayer(precinctBoundary)
                                                                             })
                    loadjscssfile("./js/geojson/PrecinctsPart28.js", "js",function (){
                                                                             // console.log(precincts28)
                                                                             var precinctBoundary = L.geoJson(precincts28, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart29.js", "js",function (){
                                                                                     // console.log(precincts29)
                                                                                     var precinctBoundary = L.geoJson(precincts29, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart30.js", "js",function (){
                                                                             // console.log(precincts30)
                                                                             var precinctBoundary = L.geoJson(precincts30, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })
                    loadjscssfile("./js/geojson/PrecinctsPart31.js", "js",function (){
                                                                                     // console.log(precincts31)
                                                                                     var precinctBoundary = L.geoJson(precincts31, {
                                                                                        style: precinctStyle,
                                                                                        onEachFeature: precinctEffects
                                                                                     }).addTo(map)
                                                                                     precLayer.addLayer(precinctBoundary)
                                                                                 })
                    loadjscssfile("./js/geojson/PrecinctsPart32.js", "js",function (){
                                                                             // console.log(precincts32)
                                                                             var precinctBoundary = L.geoJson(precincts32, {
                                                                                style: precinctStyle,
                                                                                onEachFeature: precinctEffects
                                                                             })
                                                                             precLayer.addLayer(precinctBoundary)
                                                                         })



                }


			break;

	}


}
