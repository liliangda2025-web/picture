<template>
  <div class="sky-realm" :class="{ 'is-immersed': activeSoul !== null }">
    <!-- 动态水彩天空背景 -->
    <div class="watercolor-sky">
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
    </div>

    <!-- 初始引导语 -->
    <transition name="fade-slow">
      <div v-if="activeSoul === null" class="sky-whisper">
        <p>现世的雨停了。</p>
        <p class="sub">抬头，触碰那些为你停留的光风。</p>
      </div>
    </transition>

    <!-- 交互涟漪层：彻底抛弃按钮，化作天空中的光斑 -->
    <div class="light-fragments">
      <div
        v-for="(soul, index) in souls"
        :key="index"
        class="fragment-anchor"
        :style="getFragmentStyle(index)"
      >
        <!-- 光斑本体 -->
        <div
          class="fragment-orb"
          :class="{ 'is-hidden': activeSoul !== null && activeSoul !== index }"
          @click="immerse(index)"
        >
          <div class="orb-core"></div>
          <div class="orb-halo"></div>
          <span class="orb-hint" v-if="activeSoul === null">{{ soul.hint }}</span>
        </div>

        <!-- 点击后扩散的纯白领域 (代替遮罩层) -->
        <div class="expansion-sphere" :class="{ 'is-expanded': activeSoul === index }"></div>
      </div>
    </div>

    <!-- 共鸣文字视界 -->
    <transition name="float-up">
      <div v-if="activeSoul !== null" class="soul-interface" key="interface">
        <div class="content-wrapper">
          <!-- 极简的竖线装饰，代替分割线 -->
          <div class="wind-line"></div>

          <h2 class="soul-name">{{ currentSoul.name }}</h2>
          <p class="soul-dialogue">{{ currentSoul.dialogue }}</p>
          <p class="soul-desc">{{ currentSoul.desc }}</p>

          <!-- 返回/苏醒交互 -->
          <div class="wake-up-text" @click="reset">
            <span>闭上眼，感受微风 (Return)</span>
            <div class="hover-underline"></div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const activeSoul = ref(null)

// 蓝白治愈系意象
const souls = ref([
  {
    hint: '晨风',
    name: '青梅 · 晨风与晴空',
    dialogue: '「 笨蛋，别再一个人撑伞了。在这个世界，我会把所有的晴天都留给你。」',
    desc: '她站在那棵熟悉的樱花树下，阳光穿过树叶落在她的白衬衫上。没有拯救世界的宏大叙事，只有清晨刚好温热的牛奶，和一句“欢迎回家”。'
  },
  {
    hint: '流云',
    name: '姐姐 · 流云与飞鸟',
    dialogue: '「 累了吗？那就什么都不要想。在这里，你不需要做一个坚强的大人。」',
    desc: '温柔而包容的存在。她的眼眸像雨后的初霁，抚平你灵魂里所有因现世内卷和焦躁而留下的伤痕。在这里，你拥有停下脚步的特权。'
  },
  {
    hint: '静水',
    name: '校花 · 倒影与涟漪',
    dialogue: '「 我跨越了维度的长河，只是为了在那个没有悲伤的未来，牵住你的手。」',
    desc: '原本遥不可及的白月光，此刻却眼底满是你的倒影。她放弃了原本世界的所有光环，只为在这个小小的避风港里，做你唯一的偏爱。'
  }
])

const currentSoul = computed(() => {
  return activeSoul.value !== null ? souls.value[activeSoul.value] : null
})

// 随机散落的光斑位置，模拟天空中的丁达尔光束或者花瓣
const getFragmentStyle = (index) => {
  const positions = [
    { top: '35%', left: '30%', delay: '0s' },
    { top: '55%', left: '50%', delay: '1.5s' },
    { top: '40%', left: '70%', delay: '3s' }
  ]
  return {
    top: positions[index].top,
    left: positions[index].left,
    animationDelay: positions[index].delay
  }
}

const immerse = (index) => {
  activeSoul.value = index
}

const reset = () => {
  activeSoul.value = null
}
</script>

<style scoped>
/* 字体设置优先使用优雅的明朝体/宋体或纤细的无衬线体 */
.sky-realm {
  position: relative;
  width: 100vw;
  height: 100vh;
  /* 干净空灵的蓝白渐变 */
  background: linear-gradient(135deg, #e0f2fe 0%, #bae6fd 50%, #f0f9ff 100%);
  overflow: hidden;
  font-family: 'Noto Serif SC', 'Songti SC', 'PingFang SC', sans-serif;
  color: #334155;
  transition: all 1s ease;
}

.sky-realm.is-immersed {
  background: #ffffff; /* 沉浸时背景褪去，化为纯白 */
}

/* --- 水彩天空与云层 (模糊化处理，营造梦境感) --- */
.watercolor-sky {
  position: absolute;
  inset: 0;
  pointer-events: none;
  filter: blur(60px);
  opacity: 0.8;
  transition: opacity 1.5s ease;
}

.sky-realm.is-immersed .watercolor-sky {
  opacity: 0.1;
}

.cloud {
  position: absolute;
  border-radius: 50%;
  animation: float-cloud 20s infinite alternate ease-in-out;
}

.cloud-1 {
  width: 50vw; height: 50vw;
  background: rgba(255, 255, 255, 0.9);
  top: -10%; left: -10%;
}
.cloud-2 {
  width: 60vw; height: 60vw;
  background: rgba(248, 250, 252, 0.8);
  bottom: -20%; right: -10%;
  animation-delay: -5s;
}
.cloud-3 {
  width: 40vw; height: 40vw;
  background: rgba(224, 242, 254, 0.7);
  top: 30%; left: 40%;
  animation-delay: -10s;
}

@keyframes float-cloud {
  0% { transform: translate(0, 0) scale(1); }
  100% { transform: translate(5%, -5%) scale(1.1); }
}

/* --- 初始引导文字 --- */
.sky-whisper {
  position: absolute;
  top: 20%;
  width: 100%;
  text-align: center;
  font-size: 1.5rem;
  letter-spacing: 6px;
  font-weight: 300;
  color: #64748b;
  z-index: 10;
  pointer-events: none;
}
.sky-whisper .sub {
  margin-top: 1rem;
  font-size: 0.9rem;
  letter-spacing: 3px;
  opacity: 0.7;
}

/* --- 交互层：光斑与扩张领域 --- */
.light-fragments {
  position: absolute;
  inset: 0;
  z-index: 20;
}

.fragment-anchor {
  position: absolute;
  transform: translate(-50%, -50%);
  animation: gently-drift 4s infinite alternate ease-in-out;
}

@keyframes gently-drift {
  0% { transform: translate(-50%, -50%) translateY(-10px); }
  100% { transform: translate(-50%, -50%) translateY(10px); }
}

/* 创新的“光斑”交互点 */
.fragment-orb {
  position: relative;
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.8s cubic-bezier(0.25, 1, 0.5, 1);
}

.fragment-orb.is-hidden {
  opacity: 0;
  pointer-events: none;
  transform: scale(0);
}

.orb-core {
  width: 8px;
  height: 8px;
  background: #ffffff;
  border-radius: 50%;
  box-shadow: 0 0 10px #ffffff, 0 0 20px #bae6fd;
  transition: all 0.3s ease;
}

.orb-halo {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.4);
  transform: scale(0.5);
  opacity: 0;
  transition: all 0.5s ease;
}

.orb-hint {
  position: absolute;
  top: 60px;
  font-size: 0.8rem;
  color: #38bdf8;
  letter-spacing: 2px;
  opacity: 0.6;
  transition: all 0.3s ease;
}

.fragment-orb:hover .orb-core {
  transform: scale(1.5);
  box-shadow: 0 0 15px #ffffff, 0 0 30px #7dd3fc;
}

.fragment-orb:hover .orb-halo {
  transform: scale(1);
  opacity: 1;
  animation: ripple-pulse 1.5s infinite;
}

.fragment-orb:hover .orb-hint {
  opacity: 1;
  transform: translateY(5px);
}

@keyframes ripple-pulse {
  0% { transform: scale(0.5); opacity: 1; }
  100% { transform: scale(1.2); opacity: 0; }
}

/* 极致浪漫的核心动画：点击后的领域展开 (纯白吞噬天空) */
.expansion-sphere {
  position: absolute;
  top: 50%; left: 50%;
  width: 10px; height: 10px;
  background: #ffffff;
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
  pointer-events: none;
  z-index: -1;
  transition: transform 1.2s cubic-bezier(0.22, 1, 0.36, 1), opacity 0.4s ease;
}

.expansion-sphere.is-expanded {
  opacity: 1;
  /* 扩大到足以覆盖整个屏幕的比例 */
  transform: translate(-50%, -50%) scale(300);
  box-shadow: 0 0 50px rgba(255, 255, 255, 0.8);
}

/* --- 共鸣文字视界 (进入异世界后的界面) --- */
.soul-interface {
  position: absolute;
  inset: 0;
  z-index: 30;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.6); /* 玻璃质感叠加 */
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

.content-wrapper {
  max-width: 600px;
  padding: 2rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 风的轨迹：代替生硬的分割线 */
.wind-line {
  width: 1px;
  height: 60px;
  background: linear-gradient(to bottom, transparent, #7dd3fc, transparent);
  margin-bottom: 2rem;
}

.soul-name {
  font-size: 1.2rem;
  font-weight: 400;
  color: #0ea5e9;
  letter-spacing: 4px;
  margin-bottom: 2rem;
}

.soul-dialogue {
  font-size: 1.4rem;
  line-height: 2;
  color: #0f172a;
  margin-bottom: 2rem;
  /* 文本渲染优化，增加空气感 */
  text-shadow: 0 4px 15px rgba(186, 230, 253, 0.5);
}

.soul-desc {
  font-size: 0.95rem;
  line-height: 1.8;
  color: #64748b;
  margin-bottom: 3rem;
  font-family: sans-serif;
  font-weight: 300;
}

/* 返回现世的极简文字交互 */
.wake-up-text {
  position: relative;
  font-size: 0.85rem;
  letter-spacing: 2px;
  color: #94a3b8;
  cursor: pointer;
  padding: 10px;
  text-transform: uppercase;
  font-family: sans-serif;
}

.hover-underline {
  position: absolute;
  bottom: 0; left: 50%;
  width: 0; height: 1px;
  background: #38bdf8;
  transition: all 0.4s ease;
  transform: translateX(-50%);
}

.wake-up-text:hover {
  color: #38bdf8;
}

.wake-up-text:hover .hover-underline {
  width: 100%;
}

/* --- Vue 过渡动画 --- */
.fade-slow-enter-active,
.fade-slow-leave-active {
  transition: opacity 1.5s ease;
}
.fade-slow-enter-from,
.fade-slow-leave-to {
  opacity: 0;
}

.float-up-enter-active {
  transition: all 1.2s cubic-bezier(0.25, 1, 0.5, 1) 0.3s; /* 延迟0.3s等待白圈扩散 */
}
.float-up-leave-active {
  transition: all 0.8s ease;
}
.float-up-enter-from {
  opacity: 0;
  transform: translateY(30px);
}
.float-up-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>
