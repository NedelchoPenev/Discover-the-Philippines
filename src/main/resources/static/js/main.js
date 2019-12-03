AOS.init({
  duration: 800,
  easing: 'slide',
  once: false
});

$('#summernote').summernote({
  tabsize: 2,
  height: 300
});

$('.table .delBtn').on('click',function(event){
    event.preventDefault();
    var href=$(this).attr('href');
    $('#deleteModal #delRef').attr('href', href);
    $('#deleteModal').modal();
});

$('.table .roleBtn').on('click',function(event){
    event.preventDefault();
    var href=$(this).attr('href');
    $('#setRoleModal form').attr('action', href);
});

$("#country_selector").countrySelect({
  defaultCountry: "ph",
  // onlyCountries: ['us', 'gb', 'ch', 'ca', 'do'],
  // responsiveDropdown: true
  // preferredCountries: ['ca', 'gb', 'us']
});

// menu fixed js code
$(window).scroll(function () {
  var window_top = $(window).scrollTop() + 1;
  if (window_top > 50) {
    $('.main_menu_iner').addClass('menu_fixed animated fadeInDown');
  } else {
    $('.main_menu_iner').removeClass('menu_fixed animated fadeInDown');
  }
});

$(".image-checkbox").on('click', function(){
  $('.image-checkbox').removeClass('selected');
  $('.fa-check').attr('hidden',true);
  $('.main-image-input').prop("disabled", true);

  $(this).addClass('selected');
  $(this).find('.fa-check').attr('hidden',false);
  $(this).find('.main-image-input').prop("disabled", false);
});

$(".delete-image-btn").on('click', function () {
  let element = $(this);
  let imageId = $(this).attr('id');
  $.ajax({
    url: "delete-picture",
    type: "POST",
    data: {thingToDoId: thingToDoId, imageId: imageId},
    success: function (response) {
      location.href = thingToDoId;
      $(element).notify(`Successfully delete image with id: ${response}`, 'success');
    },
    error: function (error, textStatus, errorThrown) {
      $(element).notify(error.responseJSON.message, {position: "right"});
    }
  })
});

$(document).ready(function() {
  $("#hotel").click(function () {
    $("#place-data").html("<h1 class=\"d-flex justify-content-center mb-5\">Find the perfect hotel</h1>\n" +
      "        <div class=\"container\">\n" +
      hotels +
      "        </div>");
    $('html,body').animate({
        scrollTop: $("#place-data").offset().top
      },
      'slow');
  });

  $("#map").click(function () {
    $("#place-data").html("<h1 class=\"d-flex justify-content-center mb-5\">Find the best location</h1>\n" +
      "        <div class=\"d-flex justify-content-center\">\n" +
      onMap +
      "        </div>");
    $('html,body').animate({
        scrollTop: $("#place-data").offset().top
      },
      'slow');
  });
});

$("time.timeago").timeago();

// jQuery(document).ready(function ($) {
//
//   "use strict";
//
//   var siteMenuClone = function () {
//
//     $('.js-clone-nav').each(function () {
//       var $this = $(this);
//       $this.clone().attr('class', 'site-nav-wrap').appendTo('.site-mobile-menu-body');
//     });
//
//
//     setTimeout(function () {
//
//       var counter = 0;
//       $('.site-mobile-menu .has-children').each(function () {
//         var $this = $(this);
//
//         $this.prepend('<span class="arrow-collapse collapsed">');
//
//         $this.find('.arrow-collapse').attr({
//           'data-toggle': 'collapse',
//           'data-target': '#collapseItem' + counter,
//         });
//
//         $this.find('> ul').attr({
//           'class': 'collapse',
//           'id': 'collapseItem' + counter,
//         });
//
//         counter++;
//
//       });
//
//     }, 1000);
//
//     $('body').on('click', '.arrow-collapse', function (e) {
//       var $this = $(this);
//       if ($this.closest('li').find('.collapse').hasClass('show')) {
//         $this.removeClass('active');
//       } else {
//         $this.addClass('active');
//       }
//       e.preventDefault();
//
//     });
//
//     $(window).resize(function () {
//       var $this = $(this),
//         w = $this.width();
//
//       if (w > 768) {
//         if ($('body').hasClass('offcanvas-menu')) {
//           $('body').removeClass('offcanvas-menu');
//         }
//       }
//     })
//
//     $('body').on('click', '.js-menu-toggle', function (e) {
//       var $this = $(this);
//       e.preventDefault();
//
//       if ($('body').hasClass('offcanvas-menu')) {
//         $('body').removeClass('offcanvas-menu');
//         $this.removeClass('active');
//       } else {
//         $('body').addClass('offcanvas-menu');
//         $this.addClass('active');
//       }
//     })
//
//     // click outisde offcanvas
//     $(document).mouseup(function (e) {
//       var container = $(".site-mobile-menu");
//       if (!container.is(e.target) && container.has(e.target).length === 0) {
//         if ($('body').hasClass('offcanvas-menu')) {
//           $('body').removeClass('offcanvas-menu');
//         }
//       }
//     });
//   };
//   siteMenuClone();
//
//   var siteMagnificPopup = function () {
//     $('.image-popup').magnificPopup({
//       type: 'image',
//       closeOnContentClick: true,
//       closeBtnInside: false,
//       fixedContentPos: true,
//       mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
//       gallery: {
//         enabled: true,
//         navigateByImgClick: true,
//         preload: [0, 1] // Will preload 0 - before current, and 1 after the current image
//       },
//       image: {
//         verticalFit: true
//       },
//       zoom: {
//         enabled: true,
//         duration: 300 // don't foget to change the duration also in CSS
//       }
//     });
//
//     $('.popup-youtube, .popup-vimeo, .popup-gmaps').magnificPopup({
//       disableOn: 700,
//       type: 'iframe',
//       mainClass: 'mfp-fade',
//       removalDelay: 160,
//       preloader: false,
//
//       fixedContentPos: false
//     });
//   };
//   siteMagnificPopup();
//
//
//   var siteCarousel = function () {
//     if ($('.nonloop-block-13').length > 0) {
//       $('.nonloop-block-13').owlCarousel({
//         center: false,
//         items: 1,
//         loop: true,
//         stagePadding: 0,
//         margin: 0,
//         autoplay: true,
//         nav: true,
//         navText: ['<span class="icon-arrow_back">', '<span class="icon-arrow_forward">'],
//         responsive: {
//           600: {
//             margin: 0,
//             nav: true,
//             items: 2
//           },
//           1000: {
//             margin: 0,
//             stagePadding: 0,
//             nav: true,
//             items: 3
//           },
//           1200: {
//             margin: 0,
//             stagePadding: 0,
//             nav: true,
//             items: 4
//           }
//         }
//       });
//     }
//
//     $('.slide-one-item').owlCarousel({
//       center: false,
//       items: 1,
//       loop: true,
//       stagePadding: 0,
//       margin: 0,
//       autoplay: true,
//       pauseOnHover: false,
//       nav: true,
//       navText: ['<span class="icon-keyboard_arrow_left">', '<span class="icon-keyboard_arrow_right">']
//     });
//   };
//   siteCarousel();
//
//   var siteDatePicker = function () {
//
//     if ($('.datepicker').length > 0) {
//       $('.datepicker').datepicker();
//     }
//
//   };
//   siteDatePicker();
// });