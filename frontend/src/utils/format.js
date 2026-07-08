/* Persian-friendly formatting + label dictionaries */

const FA_DIGITS = ['۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'];

/** Convert any number/string to Persian digits. */
export function toFa(value) {
  if (value === null || value === undefined) return '—';
  return String(value).replace(/\d/g, (d) => FA_DIGITS[+d]);
}

/** Group thousands then convert to Persian digits. */
export function faNumber(value) {
  if (value === null || value === undefined || value === '') return '—';
  const n = Number(value);
  if (Number.isNaN(n)) return toFa(value);
  return toFa(n.toLocaleString('en-US'));
}

/** Currency (Toman) */
export function faCurrency(value) {
  if (value === null || value === undefined) return '—';
  return faNumber(value) + ' تومان';
}

/** Format an ISO/local date to a short Jalali-looking Persian date.
 *  We avoid external deps; use Intl with fa-IR-u-ca-persian when available. */
export function faDate(value) {
  if (!value) return '—';
  try {
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return toFa(value);
    const parts = new Intl.DateTimeFormat('fa-IR-u-ca-persian', {
      year: 'numeric', month: 'long', day: 'numeric',
    }).formatToParts(d);
    const get = (t) => parts.find((p) => p.type === t)?.value || '';
    return `${get('year')} ${get('month')} ${get('day')}`;
  } catch {
    return toFa(String(value).slice(0, 10));
  }
}

export function faTime(value) {
  if (!value) return '';
  try {
    const d = new Date(`1970-01-01T${value}`);
    return new Intl.DateTimeFormat('fa-IR', { hour: '2-digit', minute: '2-digit' }).format(d);
  } catch {
    return toFa(String(value).slice(0, 5));
  }
}

export function faDateTime(value) {
  if (!value) return '—';
  try {
    const d = new Date(value);
    return new Intl.DateTimeFormat('fa-IR', {
      dateStyle: 'medium', timeStyle: 'short',
    }).format(d);
  } catch {
    return toFa(value);
  }
}

/** Relative "time ago" in Persian. */
export function timeAgo(value) {
  if (!value) return '';
  const diff = Date.now() - new Date(value).getTime();
  const min = Math.round(diff / 60000);
  if (min < 1) return 'هم‌اکنون';
  if (min < 60) return `${toFa(min)} دقیقه پیش`;
  const hr = Math.round(min / 60);
  if (hr < 24) return `${toFa(hr)} ساعت پیش`;
  const day = Math.round(hr / 24);
  if (day < 30) return `${toFa(day)} روز پیش`;
  return faDate(value);
}

/** Build initials from a full name (first letters of up to 2 words). */
export function initials(fullName = '') {
  const parts = fullName.trim().split(/\s+/).filter(Boolean);
  if (!parts.length) return '؟';
  if (parts.length === 1) return parts[0].slice(0, 2);
  return (parts[0][0] || '') + (parts[1][0] || '');
}

/* ---------- Label dictionaries (mirror backend enums) ---------- */
export const LABELS = {
  appointmentStatus: {
    SCHEDULED: { label: 'رزرو شده', tone: 'info' },
    CHECK_IN: { label: 'پذیرش شده', tone: 'warning' },
    IN_PROGRESS: { label: 'در حال ویزیت', tone: 'brand' },
    COMPLETED: { label: 'تکمیل شده', tone: 'success' },
    CANCELLED: { label: 'لغو شده', tone: 'danger' },
    NO_SHOW: { label: 'عدم مراجعه', tone: 'neutral' },
  },
  appointmentType: {
    IN_PERSON: 'حضوری',
    VIDEO: 'ویدیویی',
    PHONE: 'تلفنی',
    EMERGENCY: 'اورژانسی',
  },
  patientStatus: {
    ACTIVE: { label: 'فعال', tone: 'success' },
    ARCHIVED: { label: 'آرشیو', tone: 'neutral' },
    DECEASED: { label: 'فوت شده', tone: 'danger' },
    TRANSFERRED: { label: 'منتقل شده', tone: 'warning' },
    INACTIVE: { label: 'غیرفعال', tone: 'neutral' },
  },
  gender: {
    MAN: 'مرد', FEMALE: 'زن', OTHER: 'سایر', UNKNOWN: 'نامشخص',
  },
  bloodType: {
    A_POSITIVE: 'A+', A_NEGATIVE: 'A-', B_POSITIVE: 'B+', B_NEGATIVE: 'B-',
    AB_POSITIVE: 'AB+', AB_NEGATIVE: 'AB-', O_POSITIVE: 'O+', O_NEGATIVE: 'O-',
  },
  nursePosition: {
    HEAD_NURSE: 'سرویس‌دار', REGISTERED_NURSE: 'پرستار رسمی',
    PRACTICAL_NURSE: 'پرستار کمک', NURSE_AIDE: 'بهیار', MIDWIFE: 'ماما',
  },
  dayOfWeek: {
    SATURDAY: 'شنبه', SUNDAY: 'یکشنبه', MONDAY: 'دوشنبه',
    TUESDAY: 'سه‌شنبه', WEDNESDAY: 'چهارشنبه', THURSDAY: 'پنجشنبه', FRIDAY: 'جمعه',
  },
};

export function statusLabel(group, value) {
  const entry = LABELS[group]?.[value];
  if (!entry) return { label: value || '—', tone: 'neutral' };
  return typeof entry === 'string' ? { label: entry, tone: 'neutral' } : entry;
}

export const SPECIALITIES = {
  CARDIOLOGY: 'قلب و عروق', INTERNAL_MEDICINE: 'داخلی', PEDIATRICS: 'اطفال',
  NEUROLOGY: 'مغز و اعصاب', GASTROENTEROLOGY: 'گوارش', ENDOCRINOLOGY: 'غدد',
  NEPHROLOGY: 'کلیه', PULMONOLOGY: 'ریه', RHEUMATOLOGY: 'روماتولوژی',
  INFECTIOUS_DISEASES: 'عفونی', GENERAL_SURGERY: 'جراحی عمومی', CARDIAC_SURGERY: 'جراحی قلب',
  NEUROSURGERY: 'جراحی مغز و اعصاب', ORTHOPEDICS: 'ارتوپدی', PLASTIC_SURGERY: 'جراحی پلاستیک',
  RADIOLOGY: 'رادیولوژی', PATHOLOGY: 'پاتولوژی', ANESTHESIOLOGY: 'بیهوشی',
  EMERGENCY_MEDICINE: 'اورژانس', FAMILY_MEDICINE: 'پزشکی خانواده', PSYCHIATRY: 'روان‌پزشکی',
};

export function specialityLabel(value) {
  return SPECIALITIES[value] || (value ? toFa(value) : '—');
}

export const DEPT_EMOJI = {
  CARD: '🫀', NEUR: '🧠', ORTH: '🦴', PEDS: '👶', DERM: '🧴',
  DENT: '🦷', EYE: '👁️', RAD: '🩻', EMERG: '🚑', ICU: '🏥', LAB: '🔬',
};

export function deptEmoji(code = '') {
  const key = (code || '').slice(0, 4).toUpperCase();
  return DEPT_EMOJI[key] || '🏥';
}
