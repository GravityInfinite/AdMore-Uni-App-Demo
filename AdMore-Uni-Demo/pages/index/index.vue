<template>
	<view class="content">
		<div class="panel">
			<button class="bt" @click="initSdk">初始化SDK</button>
			<button class="bt" @click="enableDebug">开启debug模式</button>
		</div>

		<label>激励视频</label>
		<div class="panel">
			<button class="bt" @click="loadReward">load</button>
			<button class="bt" @click="isRewardReady">isReady</button>
			<button class="bt" @click="checkRewardStatus">status</button>
			<button class="bt" @click="showReward">show</button>
		</div>

		<label>插屏广告</label>
		<div class="panel">
			<button class="bt" @click="loadInterstitial">load</button>
			<button class="bt" @click="isInterstitialReady">isReady</button>
			<button class="bt" @click="checkInterstitialStatus">status</button>
			<button class="bt" @click="showInterstitial">show</button>
		</div>

		<label>信息流广告</label>
		<div class="panel">
			<button class="bt" @click="loadNative">load</button>
			<button class="bt" @click="isNativeReady">isReady</button>
			<button class="bt" @click="checkNativeStatus">status</button>
			<button class="bt" @click="showNative">show</button>
			<button class="bt" @click="removeNative">remove</button>
		</div>

		<label>banner广告</label>
		<div class="panel">
			<button class="bt" @click="loadBanner">load</button>
			<button class="bt" @click="isBannerReady">isReady</button>
			<button class="bt" @click="checkBannerStatus">status</button>
			<button class="bt" @click="showBanner">show</button>
			<button class="bt" @click="removeBanner">remove</button>
		</div>

	</view>
</template>

<script>
	import {
		APP_ID,
		APP_KEY,
		TEST_USER_ID,
		REWARD_PLACEMENT_ID,
		INTERSTITIAL_PLACEMENT_ID,
		NATIVE_PLACEMENT_ID,
		BANNER_PLACEMENT_ID,
		TOAST_SHOW_TIME
	} from '../../common/constant.js'
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {

		},
		methods: {
			rewardCallback(result) {
				console.log("admore_reward_callback");
				console.log(result);
				switch (result.functionName) {
					case 'onRewardedVideoAdLoaded':
						console.log('onRewardedVideoAdLoaded');
						break;
					case 'onRewardedVideoAdFailed':
						console.log('onRewardedVideoAdFailed');
						break;
					case 'onRewardedVideoAdPlayStart':
						console.log('onRewardedVideoAdPlayStart');
						break;
					case 'onRewardedVideoAdPlayEnd':
						console.log('onRewardedVideoAdPlayEnd');
						break;
					case 'onRewardedVideoAdPlayFailed':
						console.log('onRewardedVideoAdPlayFailed');
						break;
					case 'onRewardedVideoAdClosed':
						console.log('onRewardedVideoAdClosed');
						break;
					case 'onRewardedVideoAdPlayClicked':
						console.log('onRewardedVideoAdPlayClicked');
						break;
					case 'onReward':
						console.log('onReward');
						break;
				}
			},
			interstitialCallback(result) {
				console.log("admore_interstitial_callback");
				console.log(result);
				switch (result.functionName) {
					case 'onInterstitialAdLoaded':
						console.log('onInterstitialAdLoaded');
						break;
					case 'onInterstitialAdClicked':
						console.log('onInterstitialAdClicked');
						break;
					case 'onInterstitialAdShow':
						console.log('onInterstitialAdShow');
						break;
					case 'onInterstitialAdClose':
						console.log('onInterstitialAdClose');
						break;
					case 'onInterstitialAdVideoStart':
						console.log('onInterstitialAdVideoStart');
						break;
					case 'onInterstitialAdVideoEnd':
						console.log('onInterstitialAdVideoEnd');
						break;
					case 'onInterstitialAdVideoError':
						console.log('onInterstitialAdVideoError');
						break;
					case 'onReward':
						console.log('onReward');
						break;
				}
			},
			nativeCallback(result) {
				console.log("admore_native_callback");
				console.log(result);
				switch (result.functionName) {
					case 'onNativeAdLoaded':
						console.log('onNativeAdLoaded');
						break;
					case 'onNativeAdShow':
						console.log('onNativeAdShow');
						break;
					case 'onNativeAdClicked':
						console.log('onNativeAdClicked');
						break;
					case 'onNativeAdVideoStart':
						console.log('onNativeAdVideoStart');
						break;
					case 'onNativeAdVideoEnd':
						console.log('onNativeAdVideoEnd');
						break;
					case 'onNativeAdVideoProgress':
						console.log('onNativeAdVideoProgress');
						break;
					case 'onNativeAdVideoError':
						console.log('onNativeAdVideoError');
						break;
					case 'onRenderFail':
						console.log('onRenderFail');
						break;
					case 'onDislikeRemoved':
						console.log('onDislikeRemoved');
						break;
				}
			},
			bannerCallback(result) {
				console.log("admore_banner_callback");
				console.log(result);
				switch (result.functionName) {
					case 'onBannerAdLoaded':
						console.log('onBannerAdLoaded');
						break;
					case 'onBannerRenderFail':
						console.log('onBannerRenderFail');
						break;
					case 'onBannerAdClicked':
						console.log('onBannerAdClicked');
						break;
					case 'onBannerAdShow':
						console.log('onBannerAdShow');
						break;
					case 'onDislikeRemoved':
						console.log('onDislikeRemoved');
						break;
				}
			},
			initSdk() {
				console.log('admore_init_click');
				const admorePlugin = uni.requireNativePlugin('AdMoreModule')
				admorePlugin.initSDK(APP_ID, APP_KEY, TEST_USER_ID, data => {
					console.log('admore_init_result ' + data);
				})
			},
			enableDebug() {
				const admorePlugin = uni.requireNativePlugin('AdMoreModule');
				// 开启debug模式
				admorePlugin.setLogDebug(true);
			},
			
			// 激励视频
			loadReward() {
				console.log('loadReward');
				const admoreRewardPlugin = uni.requireNativePlugin('AdMoreRewardModule')
				admoreRewardPlugin.load(REWARD_PLACEMENT_ID, "{}", this.rewardCallback)
			},

			isRewardReady() {
				const admoreRewardPlugin = uni.requireNativePlugin('AdMoreRewardModule')
				const isReady = admoreRewardPlugin.isAdReady(REWARD_PLACEMENT_ID);
				console.log(isReady);
				uni.showToast({
					icon: 'none',
					title: '是否ready: ' + isReady,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},
			
			checkRewardStatus() {
				const admoreRewardPlugin = uni.requireNativePlugin('AdMoreRewardModule')
				const statusJson = admoreRewardPlugin.checkAdStatus(REWARD_PLACEMENT_ID);
				console.log(statusJson);
				uni.showToast({
					icon: 'none',
					title: 'status: ' + statusJson,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},

			showReward() {
				const admoreRewardPlugin = uni.requireNativePlugin('AdMoreRewardModule')
				admoreRewardPlugin.show(REWARD_PLACEMENT_ID, this.rewardCallback)
			},
			
			// 插屏
			loadInterstitial() {
				console.log('loadInterstitial');
				const admoreInterstitialPlugin = uni.requireNativePlugin('AdMoreInterstitialModule')
				admoreInterstitialPlugin.load(INTERSTITIAL_PLACEMENT_ID, "{}", this.interstitialCallback)
			},
			
			isInterstitialReady() {
				const admoreInterstitialPlugin = uni.requireNativePlugin('AdMoreInterstitialModule')
				const isReady = admoreInterstitialPlugin.isAdReady(INTERSTITIAL_PLACEMENT_ID);
				console.log(isReady);
				uni.showToast({
					icon: 'none',
					title: '是否ready: ' + isReady,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},
			
			checkInterstitialStatus() {
				const admoreInterstitialPlugin = uni.requireNativePlugin('AdMoreInterstitialModule')
				const statusJson = admoreInterstitialPlugin.checkAdStatus(INTERSTITIAL_PLACEMENT_ID);
				console.log(statusJson);
				uni.showToast({
					icon: 'none',
					title: 'status: ' + statusJson,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},

			showInterstitial() {
				const admoreInterstitialPlugin = uni.requireNativePlugin('AdMoreInterstitialModule')
				admoreInterstitialPlugin.show(INTERSTITIAL_PLACEMENT_ID, this.interstitialCallback)
			},
			
			// 信息流
			loadNative() {
				console.log('loadNative');
				const admoreNativePlugin = uni.requireNativePlugin('AdMoreNativeModule')
				admoreNativePlugin.load(NATIVE_PLACEMENT_ID, "{}", this.nativeCallback)
			},
			
			isNativeReady() {
				const admoreNativePlugin = uni.requireNativePlugin('AdMoreNativeModule')
				const isReady = admoreNativePlugin.isAdReady(NATIVE_PLACEMENT_ID);
				console.log(isReady);
				uni.showToast({
					icon: 'none',
					title: '是否ready: ' + isReady,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},
			
			checkNativeStatus() {
				const admoreNativePlugin = uni.requireNativePlugin('AdMoreNativeModule')
				const statusJson = admoreNativePlugin.checkAdStatus(NATIVE_PLACEMENT_ID);
				console.log(statusJson);
				uni.showToast({
					icon: 'none',
					title: 'status: ' + statusJson,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},

			showNative() {
				const admoreNativePlugin = uni.requireNativePlugin('AdMoreNativeModule')
				admoreNativePlugin.show(NATIVE_PLACEMENT_ID, this.nativeCallback)
			},

			removeNative() {
				const admoreNativePlugin = uni.requireNativePlugin('AdMoreNativeModule')
				admoreNativePlugin.remove(NATIVE_PLACEMENT_ID)
			},

			// Banner
			loadBanner() {
				console.log('loadBanner');
				const admoreBannerPlugin = uni.requireNativePlugin('AdMoreBannerModule')
				admoreBannerPlugin.load(BANNER_PLACEMENT_ID, "{}", this.bannerCallback)
			},
			
			isBannerReady() {
				const admoreBannerPlugin = uni.requireNativePlugin('AdMoreBannerModule')
				const isReady = admoreBannerPlugin.isAdReady(BANNER_PLACEMENT_ID);
				console.log(isReady);
				uni.showToast({
					icon: 'none',
					title: '是否ready: ' + isReady,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},
			
			checkBannerStatus() {
				const admoreBannerPlugin = uni.requireNativePlugin('AdMoreBannerModule')
				const statusJson = admoreBannerPlugin.checkAdStatus(BANNER_PLACEMENT_ID);
				console.log(statusJson);
				uni.showToast({
					icon: 'none',
					title: 'status: ' + statusJson,
					mask: false,
					duration: TOAST_SHOW_TIME
				});
			},

			showBanner() {
				const admoreBannerPlugin = uni.requireNativePlugin('AdMoreBannerModule')
				admoreBannerPlugin.show(BANNER_PLACEMENT_ID, this.bannerCallback)
			},

			removeBanner() {
				const admoreBannerPlugin = uni.requireNativePlugin('AdMoreBannerModule')
				admoreBannerPlugin.remove(BANNER_PLACEMENT_ID)
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}

	.panel {
		display: flex;
		justify-content: space-around;
		width: 100%;
		margin-top: 10px;
		margin-bottom: 10px;
	}

	.bt {
		display: inline-block;
	}
</style>
