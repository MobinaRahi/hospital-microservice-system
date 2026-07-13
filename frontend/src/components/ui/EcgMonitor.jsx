import { useRef, useEffect } from 'react';

/**
 * Realistic ECG (heartbeat) monitor rendered on <canvas>.
 * Generates a classic PQRST waveform that scrolls continuously with a
 * glowing leading dot and grid — works in both light & dark themes.
 */
export default function EcgMonitor({ bpm = 78, className = '' }) {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    let width = 0, height = 0, dpr = 1;
    let buffer = [];
    let sweepAccum = 0;
    let rafId = null;
    let last = performance.now();
    let phaseAccum = 0; // continuous phase accumulator

    function resize() {
      dpr = Math.min(window.devicePixelRatio || 1, 2);
      const rect = canvas.getBoundingClientRect();
      width = Math.max(1, Math.floor(rect.width));
      height = Math.max(1, Math.floor(rect.height));
      canvas.width = width * dpr;
      canvas.height = height * dpr;
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
      if (buffer.length !== width) buffer = new Array(width).fill(0);
    }

    /** One realistic ECG cycle value for phase t ∈ [0,1). */
    function ecgSample(t) {
      const g = (center, w, amp) => amp * Math.exp(-Math.pow((t - center) / w, 2));
      return (
        g(0.16, 0.025, 0.12) +   // P wave
        g(0.245, 0.012, -0.10) + // Q dip
        g(0.265, 0.012, -0.95) + // R spike
        g(0.295, 0.014, 0.32) +  // S dip
        g(0.42, 0.05, -0.20)     // T wave
      );
    }

    function drawGrid() {
      const step = 26;
      ctx.save();
      ctx.lineWidth = 1;
      ctx.strokeStyle = 'rgba(45, 212, 191, 0.08)';
      ctx.beginPath();
      for (let x = 0; x <= width; x += step) { ctx.moveTo(x, 0); ctx.lineTo(x, height); }
      for (let y = 0; y <= height; y += step) { ctx.moveTo(0, y); ctx.lineTo(width, y); }
      ctx.stroke();
      ctx.strokeStyle = 'rgba(45, 212, 191, 0.14)';
      ctx.beginPath();
      for (let x = 0; x <= width; x += step * 5) { ctx.moveTo(x, 0); ctx.lineTo(x, height); }
      for (let y = 0; y <= height; y += step * 5) { ctx.moveTo(0, y); ctx.lineTo(width, y); }
      ctx.stroke();
      ctx.restore();
    }

    function frame(now) {
      const dt = Math.min(50, now - last);
      last = now;

      const baseY = height * 0.52;
      const amp = height * 0.42;
      const speed = 120;
      sweepAccum += (speed * dt) / 1000;

      const cycleSec = 60 / bpm;
      // Use continuous phase accumulator to avoid discontinuity
      const phaseDelta = dt / (cycleSec * 1000);

      while (sweepAccum >= 1) {
        phaseAccum = (phaseAccum + phaseDelta) % 1;
        buffer.shift();
        buffer.push(baseY + ecgSample(phaseAccum) * amp + (Math.random() - 0.5) * 0.4);
        sweepAccum -= 1;
      }

      ctx.clearRect(0, 0, width, height);
      drawGrid();

      // baseline
      ctx.save();
      ctx.strokeStyle = 'rgba(45, 212, 191, 0.15)';
      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.moveTo(0, baseY);
      ctx.lineTo(width, baseY);
      ctx.stroke();
      ctx.restore();

      if (buffer.length > 1) {
        ctx.save();
        ctx.shadowBlur = 14;
        ctx.shadowColor = '#34d399';
        ctx.strokeStyle = '#34d399';
        ctx.lineWidth = 2.4;
        ctx.lineJoin = 'round';
        ctx.lineCap = 'round';
        ctx.beginPath();
        ctx.moveTo(0, buffer[0]);
        for (let i = 1; i < buffer.length; i++) ctx.lineTo(i, buffer[i]);
        ctx.stroke();
        ctx.shadowBlur = 0;
        ctx.strokeStyle = '#a7f3d0';
        ctx.lineWidth = 1.1;
        ctx.beginPath();
        ctx.moveTo(0, buffer[0]);
        for (let i = 1; i < buffer.length; i++) ctx.lineTo(i, buffer[i]);
        ctx.stroke();
        const lx = buffer.length - 1;
        const ly = buffer[lx];
        ctx.shadowBlur = 18;
        ctx.shadowColor = '#6ee7b7';
        ctx.fillStyle = '#ecfdf5';
        ctx.beginPath();
        ctx.arc(lx, ly, 3.2, 0, Math.PI * 2);
        ctx.fill();
        ctx.restore();

        const grad = ctx.createLinearGradient(0, 0, width * 0.18, 0);
        grad.addColorStop(0, 'rgba(6, 11, 22, 0.85)');
        grad.addColorStop(1, 'rgba(6, 11, 22, 0)');
        ctx.save();
        ctx.fillStyle = grad;
        ctx.fillRect(0, 0, width * 0.18, height);
        ctx.restore();
      }

      rafId = requestAnimationFrame(frame);
    }

    resize();
    window.addEventListener('resize', resize);
    rafId = requestAnimationFrame(frame);

    return () => {
      cancelAnimationFrame(rafId);
      window.removeEventListener('resize', resize);
    };
  }, [bpm]);

  return <canvas ref={canvasRef} className={className} />;
}
