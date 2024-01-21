<script lang="ts">
	async function load() {
		return await fetch('http://localhost:8090/api/v1/posts').then((res) => res.json());
	}
</script>

{#await load()}
	<p>loading...</p>
{:then data}
	<h1>글 리스트</h1>
	<ul>
		{#each data.data.items as post}
			<li>
				<a href="/p/{post.id}">{post.title}</a>
			</li>
		{/each}
	</ul>
{/await}
