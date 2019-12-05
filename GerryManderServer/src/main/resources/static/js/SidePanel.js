var elecType = ['Election Type','Presidential', 'Congressional'];    //get from backend later on
var yearType = ['Election Year','2016', '2018'];
var phases = ['Select a Phase', 'Phase 0', 'Phase 1', 'Phase 2'];
var algorithms = ['Select Algorithm', 'Algorithm 1', 'Algorithm 2', 'Algorithm 3'];
var selectedMinority = 0;
// var demographics = ['White','Black','Asain','Hispanic'];
var phaseSelector = $('#phaseType');
var algorithmSelector = $('#algorithmType');
var electionType = $('#electionType');
var electionYear = $('#yearType');

$('#objectiveBtn').hide();
$('#abuttonBtn').hide();

for(var i=0; i<elecType.length; i++){
  var option = document.createElement('option');
  option.setAttribute('value', elecType[i]);
  option.text = elecType[i];
  document.getElementById('electionType').appendChild(option);
}
for(var i=0; i<yearType.length; i++){
  var option = document.createElement('option');
  option.setAttribute('value', yearType[i]);
  option.text = yearType[i];
  document.getElementById('yearType').appendChild(option);
}

for(var i=0; i<phases.length; i++){
  var option = document.createElement('option');
  option.setAttribute('value', phases[i]);
  option.text = phases[i];
  document.getElementById('phaseType').appendChild(option);
}

for(var i=0; i<algorithms.length; i++){
  var option = document.createElement('option');
  option.setAttribute('value', algorithms[i]);
  option.text = algorithms[i];
  document.getElementById('algorithmType').appendChild(option);
}
// for(var i=0; i<demographics.length; i++){
//   var checkbox = document.createElement('input');
//   checkbox.type = 'checkbox';
//   checkbox.value = demographics[i];
//   checkbox.id = demographics[i]+'-cb';
//   var label = document.createElement('label');
//   label.htmlFor = checkbox.id;
//   label.appendChild(document.createTextNode(demographics[i]));
//   document.getElementById('minoritySelect').appendChild(checkbox);
//   document.getElementById('minoritySelect').appendChild(label);
// }

var maxSelCheckbox = 2;
$(".minority-cb").on('change', function(e){
  selectedMinority = $(".minority-cb").siblings(':checked').length;
  if(selectedMinority > maxSelCheckbox){
    this.checked = false;
  }
});

function hambrgrToggle(id) {
  // console.log(id);
  let menuBtnBlock;
  if(id == 'outer-menu-btn'){
    outerMenuBtn.hide();
  }
  else{
    outerMenuBtn.show();
  }
  body.toggleClass('active-nav');
  $('#prereq-block').css('display', 'None')
  $('#abutton-block').css('display', 'None')
  $('#objectives-block').css('display', 'None')

}

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
      if(this.id == 'population-slider'){
        $("#min-population").val(ui.values[0]);
        $("#max-population").val(ui.values[1]);
      }
      else if(this.id == 'compactness-slider'){
        $("#min-compactness").val(ui.values[0]);
        $("#max-compactness").val(ui.values[1]);
      }
      else if(this.id == 'fairness-slider'){
        $("#min-fairness").val(ui.values[0]);
        $("#max-fairness").val(ui.values[1]);
      }
      else if(this.id == 'equality-slider'){
        $("#min-equality").val(ui.values[0]);
        $("#max-equality").val(ui.values[1]);
      }
      else if(this.id == 'minority-population-slider'){
        $("#min-minority-population").val(ui.values[0]);
        $("#max-minority-population").val(ui.values[1]);
      }
    }
  });
  $("#min-population").val($(".slider-range").slider("values", 0));
  $("#max-population").val($(".slider-range").slider("values", 1));
  $("#min-compactness").val($(".slider-range").slider("values", 0));
  $("#max-compactness").val($(".slider-range").slider("values", 1));
  $("#min-fairness").val($(".slider-range").slider("values", 0));
  $("#max-fairness").val($(".slider-range").slider("values", 1));
  $("#min-equality").val($(".slider-range").slider("values", 0));
  $("#max-equality").val($(".slider-range").slider("values", 1));
  $("#min-minority-population").val($(".slider-range").slider("values", 0));
  $("#max-minority-population").val($(".slider-range").slider("values", 1));

});

function text2slider(id){
  // console.log('inside sliderChanger');
  var currSlider = id.substring(id.indexOf('-')+1,id.length);
  var currMinVal, currMaxVal, min, max;
  switch(currSlider){
    case 'population':
      currSlider = $("#population-slider");
      min = $("#min-population");
      max = $("#max-population");
      break;
    case 'compactness':
      currSlider = $("#compactness-slider");
      min = $("#min-compactness");
      max = $("#max-compactness");
      break;
    case 'fairness':
      currSlider = $("#fairness-slider");
      min = $("#min-fairness");
      max = $("#max-fairness");
      break;
    case 'equality':
      currSlider = $("#equality-slider");
      min = $("#min-equality");
      max = $("#max-equality");
      break;
    case 'minority-population':
      currSlider = $("#minority-population-slider");
      min = $("#min-minority-population");
      max = $("#max-minority-population");
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

function bqToggle(btn){
  var blockquote;
  switch(btn){
    case 'prereqBtn':
      blockquote = $('#prereq-block');
      break;
    case 'abuttonBtn':
      blockquote = $('#abutton-block');
      break;
    case 'objectiveBtn':
      blockquote = $('#objectives-block');
      break;
  }
  blockquote.slideToggle(1000);
}

var prerequisites = $('#prereq-block');
prerequisites.change(function(){
  // console.log('sup im changing');
  // console.log(selectedMinority);
//  && phaseSelector.val() != 'Select a Phase'
  if(electionType.val() != 'Election Type' && electionYear.val() != 'Election Year') {
//    $('#objectiveBtn').show();
    $('#abuttonBtn').show();
    districtData.show();
  }
  else{
    $('#abuttonBtn').hide();
    $('#abutton-block').css('display', 'None')
    $('#phaseType :nth-child(0)').prop('selected', true);
    $('#objectiveBtn').hide();
    districtData.hide();
  }
});

var algorithms = $('#abutton-block');
algorithms.change(function(){
    if(phaseSelector.val() != 'Select a Phase' && phaseSelector.val() != 'Phase 0'){
        $('#objectiveBtn').show();
    }
    else{
        $('#objectiveBtn').hide();
    }
});
