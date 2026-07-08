import { useState, useEffect, useRef } from 'react';

/**
 * Animated count-up that triggers when the element scrolls into view.
 * Returns [ref, displayValue] — attach ref to the wrapping element.
 *
 * Usage:
 *   const [ref, value] = useCountUp(4820);
 *   <span ref={ref}>{value}</span>
 */
export function useCountUp(end, duration = 1600) {
  const ref = useRef(null);
  const [value, setValue] = useState(0);
  const started = useRef(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting && !started.current) {
            started.current = true;
            const start = performance.now();
            const step = (now) => {
              const p = Math.min(1, (now - start) / duration);
              const v = Math.floor((1 - Math.pow(1 - p, 3)) * end);
              setValue(v);
              if (p < 1) requestAnimationFrame(step);
            };
            requestAnimationFrame(step);
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.5 }
    );

    observer.observe(el);
    return () => observer.disconnect();
  }, [end, duration]);

  return [ref, value];
}
