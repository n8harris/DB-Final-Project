jQuery(document).ready(function ($) {

    setInterval(function () {
        moveRight();
    }, 4000);

  
	var slideCount = $('#slider ul li').length;
	var slideWidth = $('#slider ul li').width();
	var slideHeight = $('#slider ul li').height();
	var sliderUlWidth = slideCount * slideWidth;
	
	
	$('#slider ul').css({ width: sliderUlWidth, marginLeft: - slideWidth });
	
    $('#slider ul li:last-child').prependTo('#slider ul');

    function moveRight() {
        $('#slider ul').animate({
            left: - slideWidth
        }, 300, function () {
            $('#slider ul li:first-child').appendTo('#slider ul');
            $('#slider ul').css('left', '');
        });
    };
	
	$('a[href=#top]').click(function(){
	$('html, body').animate({scrollTop:0}, 'slow');
	return false;
	});

});