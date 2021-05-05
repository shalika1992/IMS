$('.menu-nav > .menu-item > .menu-link').on('click', function () {
    window.localStorage.setItem('section', $(this)[0].id);
});

$('.menu-subnav > .menu-item > .menu-link').on('click', function () {
    window.localStorage.setItem('page', $(this)[0].id);
});

$('#kt_aside_toggle').on('click', function () {
    window.localStorage.setItem('toggle-sidebar', $('#kt_body')[0].className);
});

$('#header-brand-logo').on('click', function () {
    window.localStorage.setItem('section', 'DASH-BOARD');
    window.localStorage.removeItem('page');
})

var section = window.localStorage.getItem('section');
var page = window.localStorage.getItem('page');
var toggle_sidebar = window.localStorage.getItem('toggle-sidebar');

$('#' + section).parent().addClass('menu-item-open menu-item-here');
$('#' + page).parent().addClass('menu-item-active');

$('#kt_body').addClass(toggle_sidebar);

$('#sign_out_home').on('click',function () {
    window.localStorage.removeItem('section');
    window.localStorage.removeItem('page');
});

$('#sign_out_dash').on('click',function () {
    window.localStorage.removeItem('section');
    window.localStorage.removeItem('page');
});

$('#kt_login_signin_submit').on('click',function () {
    window.localStorage.setItem('section', 'DASH-BOARD');
})

$('#DASH-BOARD').on('click',function () {
    // window.localStorage.removeItem('section');
    window.localStorage.removeItem('page');
});
