$(document).ready(function () {
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

  if ($('#all').prop('checked') === true) {
    fetchAllImages();
  }

  $('input[type=radio][name=options]').change(function () {
    let category = $(this).val();
    $('.images-data').empty();
    if (category === 'all') {
      fetchAllImages();
    } else {
      $.get(`gallery/fetch/${category}`, function (data) {
        $('.images-data').append(`<div class="col-12 my-3 text-center">
        <h3 class="mb-4 text-capitalize">By ${category}</h3>
      </div>`);
        Object.entries(data).forEach(([key, value]) => {
          $('.images-data').append(formatGalleryView(value[0].url, value.length, key, category))
        });
      })
    }
  });

  $('.selectpicker').selectpicker();
});

AOS.init({
  duration: 800,
  easing: 'slide',
  once: false
});

$('#summernote').summernote({
  toolbar: [
    ['style', ['style']],
    ['font', ['bold', 'underline', 'clear']],
    ['fontname', ['fontname']],
    ['fontsize', ['fontsize']],
    ['height', ['height']],
    ['color', ['color']],
    ['para', ['ul', 'ol', 'paragraph']],
    ['table', ['table']],
    ['insert', ['link', 'picture', 'video']],
    ['view', ['fullscreen', 'codeview', 'help', 'undo', 'redo']],
  ],
  tabsize: 2,
  height: 300
});

$('.table .delBtn, .delete-comment').on('click', function (event) {
  event.preventDefault();
  let href = $(this).attr('href');
  $('#deleteModal #delRef').attr('href', href);
  $('#deleteModal').modal();
});

$('.table .roleBtn').on('click', function (event) {
  event.preventDefault();
  let href = $(this).attr('href');
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

$(".image-checkbox").on('click', function () {
  $('.image-checkbox').removeClass('selected');
  $('.fa-check').attr('hidden', true);
  $('.main-image-input').prop("disabled", true);

  $(this).addClass('selected');
  $(this).find('.fa-check').attr('hidden', false);
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

function formatGalleryView(image, quantity, name, category) {
  return `<div class="col-md-4 my-1 px-1" data-aos="fade-up"">
        <a href='/gallery/${category}/${name}' class="d-block photo-item">
          <img src="${image}" class="img-fluid rounded w-100" alt="Image">
          <div class="photo-text-more">
            <div class="photo-text-more">
              <h3 class="heading">${name}</h3>
              <span class="meta">${quantity} Photos</span>
            </div>
          </div>
        </a>
      </div>`
}

function fetchAllImages() {
  $.get('gallery/fetch/all', function (data) {
    $('.images-data').append(`
      <div class="col-12 my-3 text-center">
        <h3 class="mb-4">All &mdash; ${data.length} Photos</h3>
      </div>
    <div class="row images-container"></div>`);

    data.forEach(function (image) {
      $('.images-container').append(`<div class="col-md-4 my-1 px-1" data-aos="fade-up">
        <a href="${image.url}" class="d-block photo-item" data-fancybox="gallery"
           data-caption="Uploaded by <a href='/users/profile/${image.uploaderUsername}'>${image.uploaderUsername}</a>">
          <img src="${image.url}" class="img-fluid rounded w-100" alt="Image">
          <div class="photo-text-more">
                <span class="icon icon-search">
                  <i class="fas fa-search"></i>
                </span>
          </div>
        </a>
      </div>`)
    })
  })
}

if ($('.container').is($('#add-pictures'))) {
  fetch('/places/fetch/all')
    .then((response) => response.json())
    .then((json) => {
      json.forEach((place) => $('#add-places')
        .append(`<option value="${place.name}">${place.name}</option>`));
    })
    .catch((err) => console.log(err));
}

if ($('.container-fluid').is($('#add-block-post'))) {
  console.log(categories);
  fetch('/category/fetch/all')
    .then((response) => response.json())
    .then((json) => {
      json.forEach((category) => $('#add-categories')
        .append(`<option value="${category.id}">${category.name}</option>`));
    }).then(() => {
    if (categories) {
      $('.selectpicker').val(categories);
    }
    $('.selectpicker').selectpicker("refresh");
  }).catch((err) => console.log(err));
}

if ($('aside').is($('.post_category_widget'))) {
  fetch('/category/fetch/top')
    .then((response) => response.json())
    .then((json) => {
      json.forEach((category) => $('.cat-list')
        .append(`<li>
            <a href="/blog/category/${category.id}" class="d-flex">
              <p>${category.name}</p>
              <p class="text-success">&nbsp(${category.postsSize})</p>
            </a>
          </li>`));
    })
    .catch((err) => console.log(err));
}

if ($('aside').is($('.instagram_feeds'))) {
  fetch('/gallery/fetch/all')
    .then((response) => response.json())
    .then((json) => {
      json.sort((i1, i2) => i2.uploadDate - i1.uploadDate)
        .slice(0, 6).forEach((image) =>
        $('.instagram_row').append(`<li>
            <a href="${image.url}" data-fancybox="gallery">
              <img class="img-fluid" src="${image.url}" alt="missing image">
            </a>
          </li>`));
    })
    .catch((err) => console.log(err));
}

if ($('aside').is($('.popular_post_widget'))) {
  fetch('/blog/fetch/top-posts')
    .then((response) => response.json())
    .then((json) => {
      json.forEach((post) => {
        let date = new Date(post.datePosted).toLocaleDateString('en-GB', {
          year: 'numeric',
          month: 'short',
          day: '2-digit'
        });

        let title = post.title.slice(0, 15);
        let trunc = post.title.length > 15 ? '...' : '';

        $('#popular_posts').append(`
        <div class="media post_item row">
          <div class="col-md-4">
            <img src="${post.headerImageUrl}" alt="post">
          </div>
          <div class="media-body col-md-8">
            <a href="/blog/${post.id}">
              <h3>${title + trunc}</h3>
            </a>
            <p>${date}</p>
            <p>${post.likesSize} likes</p>
          </div>
        </div>
          `)
      });
    })
    .catch((err) => console.log(err));
}

$("time.timeago").timeago();

$(".like-login").on("click", function () {
  $(this).notify("Please, Login to Like", 'error');
});

if (($("#login-success-notify").length)) {
  $.notify.addStyle('welcome', {
    html: "<div><span data-notify-text/></div>",
    classes: {
      base: {
        "white-space": "nowrap",
        "color": "#468847",
        "background-color": "#DFF0D8",
        "border-color": "#D6E9C6",
        "border": "1px solid #fbeed5",
        "border-radius": "4px",
        "font-weight": "bold",
        "padding": "15px",
        "font-size": "30px"
      }
    }
  });

  let username = $("#login-success-notify").text();
  $.notify(`Welcome back, ${username}!`, {position: 'top center', style: 'welcome'});
  $("#login-success-notify").remove();
}