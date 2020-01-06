
/* Effects of Tabs */
$(".tabs .tab-links a").on('click', function(e) {
  var selectedTab = $(this).attr('href');
  $(selectedTab).show().siblings().hide();
  $(this).parent('li').addClass('active').siblings().removeClass('active');
});

/* Allow only MAX 2 Minorities to be selected */
$(".minority-cb").on('change', function(e){
  selectedMinority = $(".minority-cb").siblings(':checked').length;
  if(selectedMinority > maxSelCheckbox){
    this.checked = false;
  }
});

/* Switches between Hamburger Button */
function hambrgrToggle(id) {
  // console.log(id);

  let menuBtnBlock;
  if(id == 'outer-menu-btn'){
    outerMenuBtn.hide();
    if(stateElection == null && (electionSetting == 'congressional2016' || electionSetting == 'congressional2018') ){
      // console.log('calling burgerjax');
      burgerjax();
    }
  }
  else{
    outerMenuBtn.show();
  }
  body.toggleClass('active-nav');
  // $('#prereq-block').css('display', 'None')
  // $('#abutton-block').css('display', 'None')
  // $('#objectives-block').css('display', 'None')

}

function burgerjax(){
  $.ajax({
    url:"http://localhost:8080/stateData",
    data: {
      electionType: electionSetting
    },
    success: function(response){
      stateElection = JSON.parse(response);
      // console.log(stateElection);
      updateStateElec(stateElection);
    },
    error:function(err){
    console.log(err)
    console.log("ERROR")
    }

});
}
function updateStateElec(props){
  // var totVotes = props.Republican + props.Democrat + props.Green + props.Libertarian;
	var stateElecData = $('#stateElecData');
	stateElecData.html(
	'<br/><h3>State Election Data </h3><br/>'
  + '<h4>Congressional District Distribution </h4>'
	+ '<b>Republican Districts: </b>' + parseFloat(props.RepublicanRatio).toFixed(2)
  + '%<br/><b>Democrat Districts: </b>' + parseFloat(props.DemocratRatio).toFixed(2)
  + '%<br/><b>Green Districts: </b>' + parseFloat(props.GreenRatio).toFixed(2)
  + '%<br/><b>Libertarian Districts: </b>' + parseFloat(props.LibertarianRatio).toFixed(2)+'%'
  // + '%<br/><h4>Voting Distribution</h4>'
  // + '<br/><b>Republican Districts: </b>' + parseFloat(props.Republican).toFixed(2)
  // + '<br/><b>Democrat Districts: </b>' + parseFloat(props.Democrat).toFixed(2)
  // + '<br/><b>Green Districts: </b>' + parseFloat(props.Green).toFixed(2)
  // + '<br/><b>Libertarian Districts: </b>' + parseFloat(props.Libertarian).toFixed(2)
);
}

$(function(){
  $(".phase0-slider").slider({
    range: false,
    orientation: "horizontal",
    min: 50,
    max: 100,
    values:[50],
    step: 1,
    slide:
      function(event,ui){
        if(this.id == 'phase0slider1'){
          $("#votingThreshold").val(ui.values[0]);
          votingThresh = ui.values[0];
          // console.log(votingThresh);
        }
        else if(this.id == 'phase0slider2'){
          $("#blocThreshold").val(ui.values[0]);
          blocThresh = ui.values[0];
          // console.log(blocThresh);
        }
      }
  });
  $("#votingThreshold").val($(".phase0-slider").slider("values", 0));
  $("#blocThreshold").val($(".phase0-slider").slider("values", 0));
});

function text2slider2(id){
    var currSlider;
    var currVal,val;
    // console.log(id);
    switch(id){
      case 'votingThreshold':
        currSlider = $('#phase0slider1');
        val = $('#votingThreshold');
        votingThresh = parseInt(val.val());
        // console.log(votingThresh);
        break;
      case 'blocThreshold':
        currSlider = $('#phase0slider2');
        val = $('#blocThreshold');
        blocThresh = parseInt(val.val());
        // console.log(blocThresh);
        break;
    }
    currVal = parseInt(val.val());

    if(currVal <= 50 || currVal > 100){
      currVal = 50;
      val.val(currVal);
    }
    // console.log(currSlider);
    currSlider.slider({
      values: [currVal]
    });
}


/* Initializing Sliders */
$(function () {
  $(".slider-range").slider({
  range: true,
  orientation: "horizontal",
  min: 0,
  max: 100,
  values: [0, 100],
  step: 1,
  slide:
    function (event, ui) {
      if (ui.values[0] == ui.values[1]) {
          return false;
      }
      if(this.id == 'fairness-slider'){
        $("#min-fairness").val(ui.values[0]);
        $("#max-fairness").val(ui.values[1]);
        fairnessMin = ui.values[0];
        fairnessMax = ui.values[1];

      }
      else if(this.id == 'reokCompact-slider'){
        $("#min-reokCompact").val(ui.values[0]);
        $("#max-reokCompact").val(ui.values[1]);
        reokCompactMin = ui.values[0];
        reokCompactMax = ui.values[1];
      }
      else if(this.id == 'convexCompact-slider'){
        $("#min-convexCompact").val(ui.values[0]);
        $("#max-convexCompact").val(ui.values[1]);
        convexCompactMin = ui.values[0];
        convexCompactMax = ui.values[1];
      }
      else if(this.id == 'countyCompact-slider'){
        $("#min-countyCompact").val(ui.values[0]);
        $("#max-countyCompact").val(ui.values[1]);
        countyCompactMin = ui.values[0];
        countyCompactMax = ui.values[1];
      }
      else if(this.id == 'edgeCompact-slider'){
        $("#min-edgeCompact").val(ui.values[0]);
        $("#max-edgeCompact").val(ui.values[1]);
        edgeCompactMin = ui.values[0];
        edgeCompactMax = ui.values[1];
      }
      else if(this.id == 'popEqual-slider'){
        $("#min-popEqual").val(ui.values[0]);
        $("#max-popEqual").val(ui.values[1]);
        popEqualMin = ui.values[0];
        popEqualMax = ui.values[1];
      }
      else if(this.id == 'popHomo-slider'){
        $("#min-popHomo").val(ui.values[0]);
        $("#max-popHomo").val(ui.values[1]);
        popHomoMin = ui.values[0];
        popHomoMax = ui.values[1];
      }
      else if(this.id == 'efficiency-slider'){
        $("#min-efficiency").val(ui.values[0]);
        $("#max-efficiency").val(ui.values[1]);
        efficiencyMin = ui.values[0];
        efficiencyMax = ui.values[1];
      }
      else if(this.id == 'competitive-slider'){
        $("#min-competitive").val(ui.values[0]);
        $("#max-competitive").val(ui.values[1]);
        competitiveMin = ui.values[0];
        competitiveMax = ui.values[1];
      }
      else if(this.id == 'republicGerry-slider'){
        $("#min-republicGerry").val(ui.values[0]);
        $("#max-republicGerry").val(ui.values[1]);
        republicGerryMin = ui.values[0];
        republicGerryMax = ui.values[1];
      }
      else if(this.id == 'democratGerry-slider'){
        $("#min-democratGerry").val(ui.values[0]);
        $("#max-democratGerry").val(ui.values[1]);
        democratGerryMin = ui.values[0];
        democratGerryMax = ui.values[1];
      }
    }
  });
  $("#min-fairness").val($(".slider-range").slider("values", 0));
  $("#max-fairness").val($(".slider-range").slider("values", 1));
  $("#min-reokCompact").val($(".slider-range").slider("values", 0));
  $("#max-reokCompact").val($(".slider-range").slider("values", 1));
  $("#min-convexCompact").val($(".slider-range").slider("values", 0));
  $("#max-convexCompact").val($(".slider-range").slider("values", 1));
  $("#min-edgeCompact").val($(".slider-range").slider("values", 0));
  $("#max-edgeCompact").val($(".slider-range").slider("values", 1));
  $("#min-popEqual").val($(".slider-range").slider("values", 0));
  $("#max-popEqual").val($(".slider-range").slider("values", 1));
  $("#min-popHomo").val($(".slider-range").slider("values", 0));
  $("#max-popHomo").val($(".slider-range").slider("values", 1));
  $("#min-efficiency").val($(".slider-range").slider("values", 0));
  $("#max-efficiency").val($(".slider-range").slider("values", 1));
  $("#min-competitive").val($(".slider-range").slider("values", 0));
  $("#max-competitive").val($(".slider-range").slider("values", 1));
  $("#min-republicGerry").val($(".slider-range").slider("values", 0));
  $("#max-republicGerry").val($(".slider-range").slider("values", 1));
  $("#min-democratGerry").val($(".slider-range").slider("values", 0));
  $("#max-democratGerry").val($(".slider-range").slider("values", 1));
  $("#min-countyCompact").val($(".slider-range").slider("values", 0));
  $("#max-countyCompact").val($(".slider-range").slider("values", 1));

});

/* Setting slider values */
function text2slider(id){
  // console.log('inside sliderChanger');
  var currSlider = id.substring(id.indexOf('-')+1,id.length);
  var currMinVal, currMaxVal, min, max;
  switch(currSlider){
    case 'fairness':
      currSlider = $("#fairness-slider");
      min = $("#min-fairness");
      max = $("#max-fairness");

      if(parseInt(min.val()) == parseInt(max.val())){
        fairnessMin = parseInt(min.val());
        fairnessMax = 100;
      }
      else{
        fairnessMin = parseInt(min.val());
        fairnessMax = parseInt(max.val());
      }
      break;
    case 'reokCompact':
      currSlider = $("#reokCompact-slider");
      min = $("#min-reokCompact");
      max = $("#max-reokCompact");

      if(parseInt(min.val()) == parseInt(max.val())){
        reokCompactMin = parseInt(min.val());
        reokCompactMax = 100;
      }
      else{
        reokCompactMin = parseInt(min.val());
        reokCompactMax = parseInt(max.val());
      }
      break;
    case 'convexCompact':
      currSlider = $("#convexCompact-slider");
      min = $("#min-convexCompact");
      max = $("#max-convexCompact");

      if(parseInt(min.val()) == parseInt(max.val())){
        convexCompactMin = parseInt(min.val());
        convexCompactMax = 100;
      }
      else{
        convexCompactMin = parseInt(min.val());
        convexCompactMax = parseInt(max.val());
      }
      break;
    case 'countyCompact':
      currSlider = $("#countyCompact-slider");
      min = $("#min-countyCompact");
      max = $("#max-countyCompact");

      if(parseInt(min.val()) == parseInt(max.val())){
        countyCompactMin = parseInt(min.val());
        countyCompactMax = 100;
      }
      else{
        countyCompactMin = parseInt(min.val());
        countyCompactMax = parseInt(max.val());
      }
      break;
    case 'edgeCompact':
      currSlider = $("#edgeCompact-slider");
      min = $("#min-edgeCompact");
      max = $("#max-edgeCompact");

      if(parseInt(min.val()) == parseInt(max.val())){
        edgeCompactMin = parseInt(min.val());
        edgeCompactMax = 100;
      }
      else{
        edgeCompactMin = parseInt(min.val());
        edgeCompactMax = parseInt(max.val());
      }
      break;
    case 'popEqual':
      currSlider = $("#popEqual-slider");
      min = $("#min-popEqual");
      max = $("#max-popEqual");

      if(parseInt(min.val()) == parseInt(max.val())){
        popEqualMin = parseInt(min.val());
        popEqualMax = parseInt(max.val());
      }
      else{
        popEqualMin = parseInt(min.val());
        popEqualMax = 100;
      }
      break;
    case 'popHomo':
      currSlider = $("#popHomo-slider");
      min = $("#min-popHomo");
      max = $("#max-popHomo");

      if(parseInt(min.val()) == parseInt(max.val())){
        popHomoMin = parseInt(min.val());
        popHomoMax = 100;
      }
      else{
        popHomoMin = parseInt(min.val());
        popHomoMax = parseInt(max.val());
      }
      break;
    case 'efficiency':
      currSlider = $("#efficiency-slider");
      min = $("#min-efficiency");
      max = $("#max-efficiency");
      efficiencyMin = parseInt(min.val());
      efficiencyMax = parseInt(max.val());
      if(parseInt(min.val()) == parseInt(max.val())){
        efficiencyMin = parseInt(min.val());
        efficiencyMax = 100;
      }
      else{
        efficiencyMin = parseInt(min.val());
        efficiencyMax = parseInt(max.val());
      }
      break;
    case 'competitive':
      currSlider = $("#competitive-slider");
      min = $("#min-competitive");
      max = $("#max-competitive");

      if(parseInt(min.val()) == parseInt(max.val())){
        competitiveMin = parseInt(min.val());
        competitiveMax = 100;
      }
      else{
        competitiveMin = parseInt(min.val());
        competitiveMax = parseInt(max.val());
      }
      break;
    case 'republicGerry':
      currSlider = $("#republicGerry-slider");
      min = $("#min-republicGerry");
      max = $("#max-republicGerry");
      if(parseInt(min.val()) == parseInt(max.val())){
        republicGerryMin = parseInt(min.val());
        republicGerryMax = 100;
      }
      else{
        republicGerryMin = parseInt(min.val());
        republicGerryMax = parseInt(max.val());
      }
      break;
    case 'democratGerry':
      currSlider = $("#democratGerry-slider");
      min = $("#min-democratGerry");
      max = $("#max-democratGerry");
      if(parseInt(min.val()) == parseInt(max.val())){
        democratGerryMin = parseInt(min.val());
        democratGerryMax = 100;
      }
      else{
        democratGerryMin = parseInt(min.val());
        democratGerryMax = parseInt(max.val());
      }
      break;
  }
  currMinVal = parseInt(min.val());
  currMaxVal = parseInt(max.val());
  if(currMinVal == currMaxVal){
    currMaxVal = 100;
    min.val(currMinVal);
    max.val(currMaxVal);
  }
  currSlider.slider({
    values: [currMinVal, currMaxVal]
  });
}


$("input[type='radio']").click(function(){
  electionSetting = $("input[name='ElectionData']:checked").val();
  // console.log(electionSetting.split(" "));
  electionYear = electionSetting.split(" ")[0];
  electionType = electionSetting.split(" ")[1];
  // console.log(electionType+ ' ' + electionSetting + ' '+ electionYear);
});

$("input[name='PhaseType']").click(function(){
  selectedPhase = $("input[name='PhaseType']:checked").val();
})

// function updatePhase1(){
//     // console.log("Phase 1 Results: ")
// //    getPhaseZeroData(electionType.val()+electionYear.val(),0.75,0.8);
//     getPhaseOneData(electionSetting,0.75,0.6,false,36,"ASIAN,PACISLAND");
//     // console.log(phase0Data)
// }

function runAlgorithm(){
  switch (selectedPhase) {
    case 'Phase 1':
        alert('Phase 1 is start...')
        // votingThresh = 0.75;
        // blocThresh = 0.6;
        // targetDisVal = 36;
        // demString = "ASIAN,PACISLAND";
        // console.log(votingThresh);
        // console.log(blocThresh);
        // console.log(targetDisVal);
        // console.log(minoritySelected);
      if(iterateMe.prop('checked') == true){
        getPhaseOneData(electionSetting,votingThresh,blocThresh, targetDisVal, minoritySelected,true,algoBegin);
      }
      else{
        getPhaseOneData(electionSetting,votingThresh,blocThresh, targetDisVal, minoritySelected,false,algoBegin);
      }
      phase1ran = 1;
      break;
    case 'Phase 2':
    if(phase1ran == 1){
      getPhaseTwoData(electionSetting,votingThresh,blocThresh, targetDisVal, minoritySelected, true);
    }
    else{
      alert("Please Run Phase 1 First")
    }
    break;

  }
}
function showMinority(){
  $('#MinorityP12').slideToggle(1000);
}

minorityOpt.on('change', function(){
  minoritySelected = '';
  $("input[name='minority-cb']:checked").each(function(){
    minoritySelected += this.value
    minoritySelected += ","
  })
  minoritySelected = minoritySelected.slice(0,-1);
  // console.log(minoritySelected);
});
