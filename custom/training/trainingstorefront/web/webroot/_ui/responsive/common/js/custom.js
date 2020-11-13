if (!window.demo) {
    window.demo = {};
}
var demo = window.demo

if (!window.ACC) {
    window.ACC={config:{encodedContextPath: {}}};
}

(function ($) {
    demo.global={
        home:function (){
            var mainBanner=$(".main-banner");
            mainBanner.slick({
                dots:true,
                infinite:true,
                speed: 300,
                slidesToShow: 1,
                slidesToScroll: 1,
                adaptiveHeight: true
            });
        },
        init: function (){
            var _self = demo.global;
            _self.home();
        }
    }
    demo.global.init();
})(jQuery);