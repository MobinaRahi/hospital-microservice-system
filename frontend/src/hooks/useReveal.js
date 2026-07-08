import { useEffect, useRef } from 'react';

/**
 * Scroll-reveal animation hook.
 * Attach the returned ref to any element; it fades/slides in when it
 * enters the viewport. Works for single elements or staggered groups
 * (children get a --d delay variable automatically).
 *
 * Usage:
 *   const ref = useReveal();
 *   <div ref={ref} className="reveal">…</div>
 *
 * For staggered grids, add className "stagger" too — children animate in sequence.
 */
export function useReveal(options = {}) {
  const ref = useRef(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    // Stagger: set incremental delay on direct children
    if (el.classList.contains('stagger')) {
      [...el.children].forEach((child, i) => {
        child.style.setProperty('--d', `${i * 0.07}s`);
      });
    }

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('in');
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: options.threshold ?? 0.12, rootMargin: options.rootMargin ?? '0px 0px -7% 0px' }
    );

    observer.observe(el);
    return () => observer.disconnect();
  }, [options.threshold, options.rootMargin]);

  return ref;
}
