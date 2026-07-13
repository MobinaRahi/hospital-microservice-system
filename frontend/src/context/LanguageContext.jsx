import { createContext, useContext, useState, useEffect } from 'react';

const LanguageContext = createContext({
  lang: 'fa',
  toggleLang: () => {},
  t: (key) => key,
});

const translations = {
  fa: {
    // Navbar
    'nav.home': 'خانه',
    'nav.doctors': 'پزشکان',
    'nav.departments': 'بخش‌ها',
    'nav.about': 'درباره ما',
    'nav.contact': 'تماس',
    'nav.track': 'پیگیری نوبت',
    'nav.book': 'رزرو نوبت',
    'nav.login': 'ورود',
    'nav.dashboard': 'داشبورد',

    // Home
    'home.hero.title': 'سلامتی ساده‌تر از همیشه',
    'home.hero.subtitle': 'نووا، پلتفرم یکپارچه درمانی که با تجربه‌ای روان، دوستانه و مطمئن، از اولین نوبت تا درمان کامل در کنار شماست.',
    'home.hero.book': 'رزرو نوبت آنلاین',
    'home.hero.departments': 'مشاهده بخش‌ها',
    'home.stats.doctors': 'پزشک متخصص',
    'home.stats.patients': 'بیمار تحت درمان',
    'home.stats.departments': 'بخش تخصصی',
    'home.stats.appointments': 'نوبت ثبت‌شده',
    'home.how.title': 'در سه گام ساده، نوبت بگیرید',
    'home.how.subtitle': 'بدون معطلی و پیچیدگی، فقط چند کلیک تا ویزیت متخصص.',
    'home.step1': 'بخش و پزشک را انتخاب کنید',
    'home.step1.desc': 'از میان ده‌ها تخصص و متخصص مجرب، مناسب‌ترین را بر اساس نیاز خود پیدا کنید.',
    'home.step2': 'زمان نوبت را رزرو کنید',
    'home.step2.desc': 'تقویم آنلاین پزشک را ببینید و زمان دلخواهتان را در چند ثانیه ثبت کنید.',
    'home.step3': 'به بیمارستان مراجعه کنید',
    'home.step3.desc': 'با کد رهگیری نوبت، بدون انتظار و مستقیماً به مطب متخصص بروید.',
    'home.services.title': 'هرآنچه برای سلامت نیاز دارید',
    'home.services.subtitle': 'از پذیرش و تشخیص تا درمان و پیگیری، همه چیز در یک پلتفرم یکپارچه.',
    'home.dept.title': 'بخش مورد نظر خود را انتخاب کنید',
    'home.doctors.title': 'با متخصصان ما آشنا شوید',
    'home.doctors.subtitle': 'تیمی مجرب و متعهد از بهترین پزشکان هر تخصص.',
    'home.cta.title': 'همین حالا نوبت خود را رزرو کنید',
    'home.cta.subtitle': 'بدون معطلی، آنلاین و در چند ثانیه — سلامت شما اولویت ماست.',
    'home.book': 'رزرو نوبت',
    'home.contact': 'تماس با پذیرش',
    'home.viewDoctors': 'مشاهده پزشکان',
    'home.viewDepartment': 'مشاهده بخش',

    // Common
    'common.search': 'جستجو',
    'common.loading': 'در حال بارگذاری…',
    'common.noData': 'موردی یافت نشد',
    'common.submit': 'ارسال',
    'common.back': 'بازگشت',
    'common.next': 'مرحله بعد',
    'common.prev': 'مرحله قبل',
    'common.save': 'ذخیره',
    'common.cancel': 'لغو',
    'common.delete': 'حذف',
    'common.edit': 'ویرایش',
    'common.view': 'مشاهده',
    'common.yes': 'بله',
    'common.no': 'خیر',

    // Booking
    'book.title': 'رزرو نوبت آنلاین',
    'book.subtitle': 'در چند مرحله ساده، نوبت ویزیت خود را ثبت کنید',
    'book.selectDoctor': 'انتخاب پزشک',
    'book.selectTime': 'انتخاب زمان',
    'book.patientInfo': 'اطلاعات بیمار',
    'book.department': 'بخش',
    'book.doctor': 'پزشک',
    'book.date': 'تاریخ',
    'book.time': 'ساعت',
    'book.fullName': 'نام و نام خانوادگی',
    'book.phone': 'شماره موبایل',
    'book.nationalId': 'کد ملی',
    'book.confirm': 'تأیید و رزرو نوبت',
    'book.success': 'نوبت شما ثبت شد!',
    'book.trackingCode': 'کد رهگیری شما',

    // Auth
    'auth.login': 'ورود به حساب',
    'auth.register': 'ثبت‌نام',
    'auth.forgot': 'فراموشی رمز؟',
    'auth.username': 'نام کاربری',
    'auth.password': 'رمز عبور',
    'auth.noAccount': 'حساب ندارید؟',
    'auth.hasAccount': 'قبلاً ثبت‌نام کرده‌اید؟',
  },
  en: {
    // Navbar
    'nav.home': 'Home',
    'nav.doctors': 'Doctors',
    'nav.departments': 'Departments',
    'nav.about': 'About Us',
    'nav.contact': 'Contact',
    'nav.track': 'Track Appointment',
    'nav.book': 'Book Appointment',
    'nav.login': 'Login',
    'nav.dashboard': 'Dashboard',

    // Home
    'home.hero.title': 'Healthcare, Simpler Than Ever',
    'home.hero.subtitle': 'Nova is an integrated medical platform providing a smooth, friendly, and reliable experience from your first appointment to complete treatment.',
    'home.hero.book': 'Book Online Appointment',
    'home.hero.departments': 'View Departments',
    'home.stats.doctors': 'Specialist Doctors',
    'home.stats.patients': 'Patients Under Care',
    'home.stats.departments': 'Departments',
    'home.stats.appointments': 'Appointments Booked',
    'home.how.title': 'Book in 3 Simple Steps',
    'home.how.subtitle': 'No hassle, just a few clicks to see a specialist.',
    'home.step1': 'Choose Department & Doctor',
    'home.step1.desc': 'Find the most suitable specialist from dozens of experienced doctors.',
    'home.step2': 'Reserve Your Time Slot',
    'home.step2.desc': 'View the doctor\'s online calendar and book your preferred time in seconds.',
    'home.step3': 'Visit the Hospital',
    'home.step3.desc': 'With your appointment tracking code, go directly to the specialist without waiting.',
    'home.services.title': 'Everything You Need for Health',
    'home.services.subtitle': 'From reception and diagnosis to treatment and follow-up, all in one platform.',
    'home.dept.title': 'Choose Your Department',
    'home.doctors.title': 'Meet Our Specialists',
    'home.doctors.subtitle': 'An experienced and dedicated team of the best doctors in every specialty.',
    'home.cta.title': 'Book Your Appointment Now',
    'home.cta.subtitle': 'Instantly, online, in seconds — Your health is our priority.',
    'home.book': 'Book Appointment',
    'home.contact': 'Contact Reception',
    'home.viewDoctors': 'View Doctors',
    'home.viewDepartment': 'View Department',

    // Common
    'common.search': 'Search',
    'common.loading': 'Loading…',
    'common.noData': 'No results found',
    'common.submit': 'Submit',
    'common.back': 'Back',
    'common.next': 'Next Step',
    'common.prev': 'Previous Step',
    'common.save': 'Save',
    'common.cancel': 'Cancel',
    'common.delete': 'Delete',
    'common.edit': 'Edit',
    'common.view': 'View',
    'common.yes': 'Yes',
    'common.no': 'No',

    // Booking
    'book.title': 'Online Appointment Booking',
    'book.subtitle': 'Book your visit in a few simple steps',
    'book.selectDoctor': 'Select Doctor',
    'book.selectTime': 'Select Time',
    'book.patientInfo': 'Patient Information',
    'book.department': 'Department',
    'book.doctor': 'Doctor',
    'book.date': 'Date',
    'book.time': 'Time',
    'book.fullName': 'Full Name',
    'book.phone': 'Phone Number',
    'book.nationalId': 'National ID',
    'book.confirm': 'Confirm & Book',
    'book.success': 'Your appointment has been booked!',
    'book.trackingCode': 'Your Tracking Code',

    // Auth
    'auth.login': 'Login to Account',
    'auth.register': 'Register',
    'auth.forgot': 'Forgot Password?',
    'auth.username': 'Username',
    'auth.password': 'Password',
    'auth.noAccount': "Don't have an account?",
    'auth.hasAccount': 'Already registered?',
  }
};

export function LanguageProvider({ children }) {
  const [lang, setLang] = useState(() => {
    try {
      return localStorage.getItem('nova-lang') || 'fa';
    } catch { return 'fa'; }
  });

  useEffect(() => {
    document.documentElement.lang = lang;
    document.documentElement.dir = lang === 'fa' ? 'rtl' : 'ltr';
    try { localStorage.setItem('nova-lang', lang); } catch {}
  }, [lang]);

  const toggleLang = () => setLang(prev => prev === 'fa' ? 'en' : 'fa');

  const t = (key) => translations[lang]?.[key] || key;

  return (
    <LanguageContext.Provider value={{ lang, toggleLang, t }}>
      {children}
    </LanguageContext.Provider>
  );
}

export function useLang() {
  return useContext(LanguageContext);
}
